/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.bean.PropertyHelper;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
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
  private boolean           pendingOperationsExecuting = false;
  private Set<IEntity>      pendingUpdatingEntities;
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
    PropertyDescriptor roleDescriptor = PropertyHelper.getPropertyDescriptor(
        entityContract, property);
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
  public void commitUnitOfWork() {
    try {
      super.commitUnitOfWork();
    } finally {
      traversedPendingOperations = false;
    }
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
  @Override
  public void performPendingOperations() {
    if (!traversedPendingOperations) {
      traversedPendingOperations = true;
      hibernateTemplate.execute(new HibernateCallback() {

        /**
         * {@inheritDoc}
         */
        public Object doInHibernate(Session session) {
          try {
            pendingOperationsExecuting = true;
            pendingUpdatingEntities = new HashSet<IEntity>();
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
                session.saveOrUpdate(sessionEntity);
                flushIsNecessary = true;
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
                session.delete(sessionEntity);
                flushIsNecessary = true;
              }
            }
            if (flushIsNecessary) {
              session.flush();
            }
            return null;
          } finally {
            pendingOperationsExecuting = false;
            pendingUpdatingEntities = null;
          }
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForUpdate(IEntity entity) {
    if (pendingOperationsExecuting) {
      if (!pendingUpdatingEntities.contains(entity)) {
        pendingUpdatingEntities.add(entity);
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
      if (owner.isPersistent()) {
        if (transientCollection instanceof Set) {
          PersistentSet persistentSet = new PersistentSet(null,
              (Set<?>) transientCollection);
          persistentSet.setOwner(owner);
          HashMap<Object, Object> snapshot = new HashMap<Object, Object>();
          if (varSnapshotCollection == null) {
            persistentSet.clearDirty();
            varSnapshotCollection = transientCollection;
          }
          for (Object snapshotCollectionElement : varSnapshotCollection) {
            snapshot.put(snapshotCollectionElement, snapshotCollectionElement);
          }
          persistentSet.setSnapshot(owner.getId(), getHibernateRoleName(owner
              .getComponentContract(), role), snapshot);
          return persistentSet;
        } else if (transientCollection instanceof List) {
          PersistentList persistentList = new PersistentList(null,
              (List<?>) transientCollection);
          persistentList.setOwner(owner);
          ArrayList<Object> snapshot = new ArrayList<Object>();
          if (varSnapshotCollection == null) {
            persistentList.clearDirty();
            varSnapshotCollection = transientCollection;
          }
          for (Object snapshotCollectionElement : varSnapshotCollection) {
            snapshot.add(snapshotCollectionElement);
          }
          persistentList.setSnapshot(owner.getId(), getHibernateRoleName(owner
              .getComponentContract(), role), snapshot);
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
      ((HibernateTransactionManager) getTransactionTemplate()
          .getTransactionManager()).setEntityInterceptor(entityInterceptor);
    }
  }

  /**
   * Locks an entity (LockMode.NONE) in current hibernate session.
   * 
   * @param entity
   *          the entity to lock.
   * @param hibernateSession
   *          the hibernate session.
   * @return true if entity was re-attached or false if the entity was already
   *         in the session.
   */
  private void lockInHibernate(IEntity entity, Session hibernateSession) {
    // Do not use get before trying to lock.
    // Get performs a DB query.
    try {
      hibernateSession.lock(entity, LockMode.NONE);
    } catch (Exception ex) {
      Object sessionEntity = hibernateSession.get(
          entity.getComponentContract(), entity.getId());
      hibernateSession.evict(sessionEntity);
      hibernateSession.lock(entity, LockMode.NONE);
    }
  }

  @SuppressWarnings("unchecked")
  private void lockInHibernateInDepth(IComponent component,
      Session hibernateSession, Set<IEntity> alreadyLocked) {
    boolean isEntity = component instanceof IEntity;
    if (!isEntity || alreadyLocked.add((IEntity) component)) {
      if (!isEntity || ((IEntity) component).isPersistent()) {
        if (isEntity) {
          lockInHibernate((IEntity) component, hibernateSession);
        }
        Map<String, Object> entityProperties = component
            .straightGetProperties();
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
  }

  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void registerForDeletion(IEntity entity) {
  // if (pendingOperationsExecuting) {
  // getHibernateTemplate().delete(entity);
  // } else {
  // super.registerForDeletion(entity);
  // }
  // }
}
