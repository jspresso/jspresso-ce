/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.backend.persistence.hibernate;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentSet;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.proxy.HibernateProxy;
import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.bean.MissingPropertyException;
import org.jspresso.framework.util.bean.PropertyHelper;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * This is the default Jspresso implementation of Hibernate-based backend
 * controller.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateBackendController extends AbstractBackendController {

  private HibernateTemplate hibernateTemplate;
  private Set<IEntity>      processedEntities;
  private boolean           traversedPendingOperations = false;

  /**
   * Whenever the entity has dirty persistent collection, make them clean to
   * workaround a "bug" with hibernate since hibernate cannot re-attach a
   * "dirty" detached collection.
   * 
   * @param componentOrEntity
   *          the entity to clean the collections dirty state of.
   */
  static void cleanPersistentCollectionDirtyState(IComponent componentOrEntity) {
    if (componentOrEntity != null) {
      // Whenever the entity has dirty persistent collection, make them
      // clean to workaround a "bug" with hibernate since hibernate cannot
      // re-attach a "dirty" detached collection.
      for (Map.Entry<String, Object> registeredPropertyEntry : componentOrEntity
          .straightGetProperties().entrySet()) {
        if (registeredPropertyEntry.getValue() instanceof PersistentCollection
            && Hibernate.isInitialized(registeredPropertyEntry.getValue())) {
          ((PersistentCollection) registeredPropertyEntry.getValue())
              .clearDirty();
        }
      }
    }
  }

  private static String getHibernateRoleName(Class<?> entityContract,
      String property) {
    // have to find the highest entity class declaring the collection role.
    PropertyDescriptor roleDescriptor;
    try {
      roleDescriptor = PropertyHelper.getPropertyDescriptor(entityContract,
          property);
    } catch (MissingPropertyException ex) {
      return null;
    }
    Class<?> propertyDeclaringClass = roleDescriptor.getReadMethod()
        .getDeclaringClass();
    Class<?> roleClass;
    if (IEntity.class.isAssignableFrom(propertyDeclaringClass)) {
      roleClass = propertyDeclaringClass;
    } else {
      roleClass = getHighestEntityClassInRole(entityContract,
          propertyDeclaringClass);
    }
    return roleClass.getName() + "." + property;
  }

  private static Class<?> getHighestEntityClassInRole(Class<?> entityContract,
      Class<?> propertyDeclaringClass) {
    for (Class<?> superInterface : entityContract.getInterfaces()) {
      if (IEntity.class.isAssignableFrom(superInterface)
          && propertyDeclaringClass.isAssignableFrom(superInterface)) {
        return getHighestEntityClassInRole(superInterface,
            propertyDeclaringClass);
      }
    }
    return entityContract;
  }

  /**
   * Allows for a new run of performPendingOperations.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void clearPendingOperations() {
    super.clearPendingOperations();
    traversedPendingOperations = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IEntity> cloneInUnitOfWork(List<IEntity> entities) {
    final List<IEntity> uowEntities = super.cloneInUnitOfWork(entities);
    hibernateTemplate.execute(new HibernateCallback() {

      public Object doInHibernate(Session session) {
        Set<IEntity> alreadyLocked = new HashSet<IEntity>();
        for (IEntity mergedEntity : uowEntities) {
          lockInHibernateInDepth(mergedEntity, session, alreadyLocked);
        }
        return null;
      }

    });
    return uowEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beginUnitOfWork() {
    processedEntities = new HashSet<IEntity>();
    super.beginUnitOfWork();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commitUnitOfWork() {
    processedEntities = null;
    if (traversedPendingOperations) {
      // We must get rid of the pending operations only in the case of a
      // successful commit.
      clearPendingOperations();
    }
    super.commitUnitOfWork();
  }

  /**
   * Gets the hibernateTemplate.
   * 
   * @return the hibernateTemplate.
   */
  public HibernateTemplate getHibernateTemplate() {
    return hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void initializePropertyIfNeeded(final IComponent componentOrEntity,
      final String propertyName) {
    boolean dirtRecorderWasEnabled = getDirtRecorder().isEnabled();
    try {
      getDirtRecorder().setEnabled(false);
      final Object initializedProperty = componentOrEntity
          .straightGetProperty(propertyName);
      if (!Hibernate.isInitialized(initializedProperty)) {
        if (initializedProperty instanceof AbstractPersistentCollection) {
          if (((AbstractPersistentCollection) initializedProperty).getSession() != null
              && ((AbstractPersistentCollection) initializedProperty)
                  .getSession().isOpen()) {
            try {
              Hibernate.initialize(initializedProperty);
              if (initializedProperty instanceof Collection<?>) {
                relinkAfterInitialization(
                    (Collection<IEntity>) initializedProperty,
                    componentOrEntity);
              }
              return;
            } catch (Exception ex) {
              // ignore the exception since we are going to re-associate the
              // entity.
            }
          }
        }

        int oldFlushMode = hibernateTemplate.getFlushMode();
        try {
          // Temporary switch to a read-only session.
          hibernateTemplate.setFlushMode(HibernateAccessor.FLUSH_NEVER);
          hibernateTemplate.execute(new HibernateCallback() {

            /**
             * {@inheritDoc}
             */
            public Object doInHibernate(Session session) {
              cleanPersistentCollectionDirtyState(componentOrEntity);
              if (componentOrEntity instanceof IEntity) {
                if (((IEntity) componentOrEntity).isPersistent()) {
                  lockInHibernate((IEntity) componentOrEntity, session);
                } else if (initializedProperty instanceof IEntity) {
                  lockInHibernate((IEntity) componentOrEntity, session);
                }
              } else if (initializedProperty instanceof IEntity) {
                // to handle initialization of component properties.
                lockInHibernate((IEntity) initializedProperty, session);
              }

              Hibernate.initialize(initializedProperty);
              if (initializedProperty instanceof Collection<?>) {
                relinkAfterInitialization(
                    (Collection<IEntity>) initializedProperty,
                    componentOrEntity);
              } else {
                relinkAfterInitialization(
                    Collections.singleton((IEntity) initializedProperty),
                    componentOrEntity);
              }
              return null;
            }
          });
        } finally {
          hibernateTemplate.setFlushMode(oldFlushMode);
        }
        super.initializePropertyIfNeeded(componentOrEntity, propertyName);
        if (initializedProperty instanceof PersistentCollection) {
          ((PersistentCollection) initializedProperty).clearDirty();
        }
      }
    } finally {
      getDirtRecorder().setEnabled(dirtRecorderWasEnabled);
    }
  }

  private void relinkAfterInitialization(Collection<IEntity> entities,
      Object owner) {
    for (IEntity entity : entities) {
      for (Map.Entry<String, Object> property : entity.straightGetProperties()
          .entrySet()) {
        if (property.getValue() instanceof IEntity) {
          if (owner.equals(property.getValue()) && owner != property.getValue()) {
            entity.straightSetProperty(property.getKey(), owner);
          }
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInitialized(Object objectOrProxy) {
    return Hibernate.isInitialized(objectOrProxy);
  }

  /**
   * {@inheritDoc}
   */
  public void performPendingOperations() {
    if (!traversedPendingOperations) {
      traversedPendingOperations = true;
      hibernateTemplate.execute(new HibernateCallback() {

        /**
         * {@inheritDoc}
         */
        public Object doInHibernate(Session session) {
          boolean flushIsNecessary = false;
          Collection<IEntity> entitiesToUpdate = getEntitiesRegisteredForUpdate();
          Collection<IEntity> entitiesToDelete = getEntitiesRegisteredForDeletion();
          List<IEntity> entitiesToClone = new ArrayList<IEntity>();
          if (entitiesToUpdate != null) {
            entitiesToClone.addAll(entitiesToUpdate);
          }
          if (entitiesToDelete != null) {
            entitiesToClone.addAll(entitiesToDelete);
          }
          List<IEntity> sessionEntities = cloneInUnitOfWork(entitiesToClone);
          Map<IEntity, IEntity> entityMap = new HashMap<IEntity, IEntity>();
          for (int i = 0; i < entitiesToClone.size(); i++) {
            entityMap.put(entitiesToClone.get(i), sessionEntities.get(i));
          }
          if (entitiesToUpdate != null) {
            for (IEntity entityToUpdate : entitiesToUpdate) {
              IEntity sessionEntity = entityMap.get(entityToUpdate);
              if (sessionEntity == null) {
                sessionEntity = entityToUpdate;
              }
              if (!processedEntities.contains(sessionEntity)) {
                processedEntities.add(sessionEntity);
                session.saveOrUpdate(sessionEntity);
                flushIsNecessary = true;
              }
            }
          }
          if (flushIsNecessary) {
            session.flush();
          }
          flushIsNecessary = false;
          // there might have been new entities to delete
          entitiesToDelete = getEntitiesRegisteredForDeletion();
          if (entitiesToDelete != null) {
            for (IEntity entityToDelete : entitiesToDelete) {
              IEntity sessionEntity = entityMap.get(entityToDelete);
              if (sessionEntity == null) {
                sessionEntity = entityToDelete;
              }
              if (!processedEntities.contains(sessionEntity)) {
                processedEntities.add(sessionEntity);
                session.delete(sessionEntity);
                flushIsNecessary = true;
              }
            }
          }
          if (flushIsNecessary) {
            session.flush();
          }
          return null;
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForDeletion(IEntity entity) {
    if (isUnitOfWorkActive()) {
      if (!processedEntities.contains(entity)) {
        processedEntities.add(entity);
        getHibernateTemplate().delete(entity);
      }
    } else {
      super.registerForDeletion(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForUpdate(IEntity entity) {
    if (isUnitOfWorkActive()) {
      if (!processedEntities.contains(entity)) {
        processedEntities.add(entity);
        getHibernateTemplate().saveOrUpdate(entity);
      }
    } else {
      super.registerForUpdate(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rollbackUnitOfWork() {
    processedEntities = null;
    try {
      super.rollbackUnitOfWork();
    } finally {
      traversedPendingOperations = false;
    }
  }

  /**
   * The configured entity factory will be configured with the necessary
   * interceptors so that the controller can be notified of entity creations.
   * <p>
   * {@inheritDoc}
   * 
   * @internal
   */
  @Override
  public void setEntityFactory(IEntityFactory entityFactory) {
    super.setEntityFactory(entityFactory);
    linkHibernateArtifacts();
  }

  /**
   * Assigns the Spring hibernate template to this backend controller. This
   * property can only be set once and should only be used by the DI container.
   * It will rarely be changed from built-in defaults unless you need to specify
   * a custom implementation instance to be used.
   * <p>
   * The configured instance is the one that will be returned by the
   * controller's <code>getHibernateTemplate()</code> method that should be used
   * by the service layer to access Hibernate.
   * 
   * @param hibernateTemplate
   *          the hibernateTemplate to set.
   */
  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    if (this.hibernateTemplate != null) {
      throw new IllegalArgumentException(
          "Spring hibernate template can only be configured once.");
    }
    this.hibernateTemplate = hibernateTemplate;
    linkHibernateArtifacts();
  }

  /**
   * The configured transaction template will be configured with the necessary
   * interceptors so that the controller can be notified of transactions.
   * <p>
   * {@inheritDoc}
   * 
   * @internal
   */
  @Override
  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    super.setTransactionTemplate(transactionTemplate);
    linkHibernateArtifacts();
  }

  /**
   * This method wraps transient collections in the equivalent hibernate ones.
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected Collection<IComponent> wrapDetachedCollection(IEntity owner,
      Collection<IComponent> transientCollection,
      Collection<IComponent> snapshotCollection, String role) {
    Collection<IComponent> varSnapshotCollection = snapshotCollection;
    if (!(transientCollection instanceof PersistentCollection)) {
      String collectionRoleName = getHibernateRoleName(
          owner.getComponentContract(), role);
      if (collectionRoleName == null) {
        // it is not an hibernate managed collection (e.g. "detachedEntities")
        return super.wrapDetachedCollection(owner, transientCollection,
            snapshotCollection, role);
      }
      if (owner.isPersistent()) {
        if (transientCollection instanceof Set) {
          PersistentSet persistentSet = new PersistentSet(null,
              (Set<?>) transientCollection);
          changeCollectionOwner(persistentSet, owner);
          HashMap<Object, Object> snapshot = new HashMap<Object, Object>();
          if (varSnapshotCollection == null) {
            persistentSet.clearDirty();
            varSnapshotCollection = transientCollection;
          }
          for (Object snapshotCollectionElement : varSnapshotCollection) {
            snapshot.put(snapshotCollectionElement, snapshotCollectionElement);
          }
          persistentSet
              .setSnapshot(owner.getId(), collectionRoleName, snapshot);
          return persistentSet;
        } else if (transientCollection instanceof List) {
          PersistentList persistentList = new PersistentList(null,
              (List<?>) transientCollection);
          changeCollectionOwner(persistentList, owner);
          ArrayList<Object> snapshot = new ArrayList<Object>();
          if (varSnapshotCollection == null) {
            persistentList.clearDirty();
            varSnapshotCollection = transientCollection;
          }
          for (Object snapshotCollectionElement : varSnapshotCollection) {
            snapshot.add(snapshotCollectionElement);
          }
          persistentList.setSnapshot(owner.getId(), collectionRoleName,
              snapshot);
          return persistentList;
        }
      }
    } else {
      if (varSnapshotCollection == null) {
        ((PersistentCollection) transientCollection).clearDirty();
      } else {
        ((PersistentCollection) transientCollection).dirty();
      }
    }
    return super.wrapDetachedCollection(owner, transientCollection,
        varSnapshotCollection, role);
  }

  private void linkHibernateArtifacts() {
    if (getHibernateTemplate() != null && getTransactionTemplate() != null
        && getEntityFactory() != null) {
      ControllerAwareEntityProxyInterceptor entityInterceptor = new ControllerAwareEntityProxyInterceptor();
      entityInterceptor.setBackendController(this);
      entityInterceptor.setEntityFactory(getEntityFactory());
      getHibernateTemplate().setEntityInterceptor(entityInterceptor);
      if (getTransactionTemplate().getTransactionManager() instanceof HibernateTransactionManager) {
        ((HibernateTransactionManager) getTransactionTemplate()
            .getTransactionManager()).setEntityInterceptor(entityInterceptor);
      }
    }
  }

  /**
   * Locks an entity (LockMode.NONE) in current hibernate session.
   * 
   * @param entity
   *          the entity to lock.
   * @param hibernateSession
   *          the hibernate session.
   */
  private void lockInHibernate(IEntity entity, Session hibernateSession) {
    // Do not use get before trying to lock.
    // Get performs a DB query.
    try {
      hibernateSession.lock(entity, LockMode.NONE);
    } catch (Exception ex) {
      IComponent sessionEntity = (IComponent) hibernateSession.get(
          entity.getComponentContract(), entity.getId());
      evictFromHibernateInDepth(sessionEntity, hibernateSession,
          new HashSet<IEntity>());
      hibernateSession.lock(entity, LockMode.NONE);
    }
  }

  @SuppressWarnings("unchecked")
  private void lockInHibernateInDepth(IComponent component,
      Session hibernateSession, Set<IEntity> alreadyLocked) {
    boolean isEntity = component instanceof IEntity;
    if (!isEntity || alreadyLocked.add((IEntity) component)) {
      if (isEntity) {
        if (((IEntity) component).isPersistent()) {
          lockInHibernate((IEntity) component, hibernateSession);
        } else {
          // Cannot simply re-attach the transient entity, so we have to saveOrUpdate it.
          registerForUpdate((IEntity) component);
        }
      }
      Map<String, Object> entityProperties = component.straightGetProperties();
      for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
        if (Hibernate.isInitialized(property.getValue())) {
          if (property.getValue() instanceof IEntity) {
            lockInHibernateInDepth((IEntity) property.getValue(),
                hibernateSession, alreadyLocked);
          } else if (property.getValue() instanceof Collection) {
            for (IComponent element : ((Collection<IComponent>) property
                .getValue())) {
              lockInHibernateInDepth(element, hibernateSession, alreadyLocked);
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void evictFromHibernateInDepth(IComponent component,
      Session hibernateSession, Set<IEntity> alreadyEvicted) {
    boolean isEntity = component instanceof IEntity;
    if (!isEntity || alreadyEvicted.add((IEntity) component)) {
      if (!isEntity || ((IEntity) component).isPersistent()) {
        if (isEntity) {
          hibernateSession.evict(component);
        }
        Map<String, Object> entityProperties = component
            .straightGetProperties();
        for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
          if (Hibernate.isInitialized(property.getValue())) {
            if (property.getValue() instanceof IEntity) {
              evictFromHibernateInDepth((IEntity) property.getValue(),
                  hibernateSession, alreadyEvicted);
            } else if (property.getValue() instanceof Collection) {
              for (IComponent element : ((Collection<IComponent>) property
                  .getValue())) {
                evictFromHibernateInDepth(element, hibernateSession,
                    alreadyEvicted);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Search Hibernate using criteria. The result is then merged into session.
   * 
   * @param <T>
   *          the entity type to return
   * @param criteria
   *          the detached criteria.
   * @param mergeMode
   *          the merge mode to use when merging back retrieved entities or null
   *          if no merge is requested.
   * @param clazz
   *          the type of the entity.
   * @return the first found entity or null;
   */
  @SuppressWarnings("unchecked")
  public <T extends IEntity> T findFirstByCriteria(DetachedCriteria criteria,
      EMergeMode mergeMode, Class<? extends T> clazz) {
    List<T> ret = findByCriteria(criteria, null, clazz);
    if (ret != null && !ret.isEmpty()) {
      if (isUnitOfWorkActive()) {
        return (T) cloneInUnitOfWork(ret.get(0));
      } else if (mergeMode != null) {
        return (T) merge(ret.get(0), mergeMode);
      }
      return ret.get(0);
    }
    return null;
  }

  /**
   * Search hibernate using criteria. The result is then merged into session.
   * 
   * @param <T>
   *          the entity type to return
   * @param criteria
   *          the detached criteria.
   * @param mergeMode
   *          the merge mode to use when merging back retrieved entities or null
   *          if no merge is requested.
   * @param clazz
   *          the type of the entity.
   * @return the first found entity or null;
   */
  @SuppressWarnings("unchecked")
  public <T extends IEntity> List<T> findByCriteria(
      final DetachedCriteria criteria, EMergeMode mergeMode,
      Class<? extends T> clazz) {
    List<IEntity> res = (List<IEntity>) getTransactionTemplate().execute(
        new TransactionCallback() {

          public Object doInTransaction(
              @SuppressWarnings("unused") TransactionStatus status) {
            return getHibernateTemplate().findByCriteria(criteria);
          }
        });
    if (res != null) {
      if (isUnitOfWorkActive()) {
        return (List<T>) cloneInUnitOfWork(res);
      } else if (mergeMode != null) {
        return (List<T>) merge(res, mergeMode);
      }
      return (List<T>) res;
    }
    return null;
  }

  /**
   * Unwrap hibernate proxy if needed.
   * 
   * @param componentOrProxy
   *          the component or proxy.
   * @return the proxy implementation if it's an hibernate proxy.
   */
  @Override
  protected IComponent unwrapProxy(IComponent componentOrProxy) {
    IComponent component;
    if (componentOrProxy instanceof HibernateProxy) {
      // we must unwrap the proxy to avoid class cast exceptions.
      // see
      // http://forum.hibernate.org/viewtopic.php?p=2323464&sid=cb4ba3a4418276e5d2fbdd6c906ba734
      component = (IComponent) ((HibernateProxy) componentOrProxy)
          .getHibernateLazyInitializer().getImplementation();
    } else {
      component = componentOrProxy;
    }
    return component;
  }

  /**
   * Reloads an entity in hibernate.
   * 
   * @param entity
   *          the entity to reload.
   */
  public void reload(IEntity entity) {
    if (entity.isPersistent()) {
      HibernateTemplate ht = getHibernateTemplate();
      merge(
          (IEntity) ht.load(entity.getComponentContract().getName(),
              entity.getId()), EMergeMode.MERGE_CLEAN_EAGER);
    }
  }

  private void changeCollectionOwner(Collection<?> persistentCollection,
      Object newOwner) {
    if (persistentCollection instanceof PersistentCollection) {
      ((PersistentCollection) persistentCollection).setOwner(newOwner);
    }
  }

  /**
   * Hibernate related cloning.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object cloneUninitializedProperty(Object owner, Object propertyValue) {
    Object clonedPropertyValue = propertyValue;
    if (propertyValue instanceof PersistentCollection) {
      if (propertyValue instanceof PersistentSet) {
        clonedPropertyValue = new PersistentSet(
            ((PersistentSet) propertyValue).getSession());
      } else if (propertyValue instanceof PersistentList) {
        clonedPropertyValue = new PersistentList(
            ((PersistentList) propertyValue).getSession());
      }
      changeCollectionOwner((Collection<?>) clonedPropertyValue, owner);
      ((PersistentCollection) clonedPropertyValue).setSnapshot(
          ((PersistentCollection) propertyValue).getKey(),
          ((PersistentCollection) propertyValue).getRole(), null);
    } else {
      if (propertyValue instanceof HibernateProxy) {
        // Unfortunately there is actually no mean of performing a shallow copy
        // of it.
      }
    }
    return clonedPropertyValue;
  }
}
