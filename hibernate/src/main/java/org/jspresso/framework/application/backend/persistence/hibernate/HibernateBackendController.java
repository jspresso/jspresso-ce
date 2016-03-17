/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.application.backend.BackendException;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.entity.CarbonEntityCloneFactory;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityRegistry;
import org.jspresso.framework.model.persistence.hibernate.entity.HibernateEntityRegistry;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * This is the default Jspresso implementation of Hibernate-based backend
 * controller.
 *
 * @author Vincent Vandenschrick
 */
public class HibernateBackendController extends AbstractBackendController {

  private SessionFactory hibernateSessionFactory;
  private FlushMode defaultTxFlushMode = FlushMode.COMMIT;
  private DataSource   noTxDataSource;

  /**
   * {@code JSPRESSO_SESSION_GLOBALS} is "JspressoSessionGlobals".
   */
  public static final String JSPRESSO_SESSION_GLOBALS          = "JspressoSessionGlobals";
  /**
   * {@code JSPRESSO_SESSION_GLOBALS_LOGIN} is "login".
   */
  public static final String JSPRESSO_SESSION_GLOBALS_LOGIN    = "login";
  /**
   * {@code JSPRESSO_SESSION_GLOBALS_LANGUAGE} is "language".
   */
  public static final String JSPRESSO_SESSION_GLOBALS_LANGUAGE = "language";

  private static final Logger LOG = LoggerFactory.getLogger(HibernateBackendController.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> List<E> cloneInUnitOfWork(List<E> entities) {
    IEntityRegistry uowEntityRegistry = getUowEntityRegistry();
    final List<E> uowEntities = super.cloneInUnitOfWork(entities);
    IEntityRegistry alreadyDetached = createEntityRegistry("detachFromHibernateInDepth");
    IEntityRegistry alreadyLocked = createEntityRegistry("lockInHibernateInDepth");
    for (IEntity uowEntity : uowEntities) {
      // prevent detach/attach entities that were previously in the UOW for performance reasons.
      // see bug jspresso-ce-#103
      if (uowEntity != null && uowEntityRegistry.get(getComponentContract(uowEntity), uowEntity.getId()) == null) {
        detachFromHibernateInDepth(uowEntity, getHibernateSession(), alreadyDetached);
        lockInHibernateInDepth(uowEntity, getHibernateSession(), alreadyLocked);
      }
    }
    return uowEntities;
  }

  /**
   * Look-ups the entity in the session 1st. If it is there, return it so that
   * it avoids an unnecessary deep carbon copy.
   *
   * @param <E>
   *     the actual entity type.
   * @param entity
   *     the source entity.
   * @return the cloned entity.
   */
  @SuppressWarnings("unchecked")
  @Override
  protected <E extends IEntity> E performUowEntityCloning(final E entity) {
    if (!isInitialized(entity) || entity.isPersistent()) {
      E sessionEntity;
      if (getHibernateSession().contains(entity)) {
        sessionEntity = entity;
      } else {
        sessionEntity = (E) getHibernateSession().load(getComponentContract(entity), entity.getId());
        if (!isInitialized(entity)) {
          return sessionEntity;
        }
        if (!isInitialized(sessionEntity)) {
          getHibernateSession().evict(sessionEntity);
          sessionEntity = null;
        }
      }
      if (sessionEntity != null) {
        CarbonEntityCloneFactory.carbonCopyComponent(entity, sessionEntity, getEntityFactory());
        return sessionEntity;
      }
    }
    // fall-back to default cloning.
    return super.performUowEntityCloning(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doBeginUnitOfWork() {
    // This is to avoid having entities attached to 2 open sessions
    // and to periodically clear the noTxSession cache.
    if (noTxSession != null) {
      noTxSession.clear();
    }
    super.doBeginUnitOfWork();
  }

  private Session noTxSession = null;

  /**
   * Retrieves the current thread-bound / tx-bound Hibernate session. this is
   * now the preferred way to perform Hibernate operations. It relies on the
   * Hibernate 3.1+ {@link SessionFactory#getCurrentSession()} method.
   *
   * @return the current thread-bound / tx-bound Hibernate session.
   */
  public Session getHibernateSession() {
    Session currentSession;
    if (isUnitOfWorkActive()) {
      try {
        currentSession = getHibernateSessionFactory().getCurrentSession();
      } catch (HibernateException ex) {
        currentSession = getNoTxSession();
      }
    } else {
      currentSession = getNoTxSession();
    }
    if (currentSession != noTxSession) {
      // we are on a transactional session.
      currentSession.setFlushMode(getDefaultTxFlushMode());
    }
    configureHibernateGlobalFilter(currentSession.enableFilter(JSPRESSO_SESSION_GLOBALS));
    return currentSession;
  }

  private Session getNoTxSession() {
    if (noTxSession == null) {
      if (noTxDataSource != null) {
        try {
          noTxSession = getHibernateSessionFactory().withOptions().connection(noTxDataSource.getConnection())
                                                    .openSession();
        } catch (SQLException ex) {
          LOG.error("Couldn't get connection from non transactional data source {}", noTxDataSource);
          throw new BackendException(ex, "Couldn't get connection from non transactional data source");
        }
      } else {
        noTxSession = getHibernateSessionFactory().openSession();
      }
      noTxSession.setFlushMode(FlushMode.MANUAL);
    }
    return noTxSession;
  }

  private void configureHibernateGlobalFilter(Filter filter) {
    String filterLanguage = null;
    if (getLocale() != null) {
      filterLanguage = getLocale().getLanguage();
    }
    if (filterLanguage == null) {
      filterLanguage = "";
    }
    String filterLogin = null;
    if (getApplicationSession().getPrincipal() != null) {
      filterLogin = getApplicationSession().getUsername();
    }
    if (filterLogin == null) {
      filterLogin = "";
    }
    filter.setParameter(JSPRESSO_SESSION_GLOBALS_LANGUAGE, filterLanguage);
    filter.setParameter(JSPRESSO_SESSION_GLOBALS_LOGIN, filterLogin);
  }

  private Session currentInitializationSession = null;

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initializePropertyIfNeeded(final IComponent componentOrEntity, final String propertyName) {
    Object propertyValue = componentOrEntity.straightGetProperty(propertyName);
    if (!isInitialized(propertyValue)) {
      // turn off dirt tracking.
      boolean dirtRecorderWasEnabled = isDirtyTrackingEnabled();
      try {
        setDirtyTrackingEnabled(false);

        boolean isSessionEntity = isSessionEntity(componentOrEntity);
        boolean isUnitOfWorkActive = isUnitOfWorkActive();

        // Never initialize session entities from UOW session
        if (!(isUnitOfWorkActive && isSessionEntity)) {
          if (propertyValue instanceof Collection<?> && propertyValue instanceof AbstractPersistentCollection) {
            if (((AbstractPersistentCollection) propertyValue).getSession() != null
                && ((AbstractPersistentCollection) propertyValue).getSession().isOpen()) {
              try {
                Hibernate.initialize(propertyValue);
                relinkAfterInitialization((Collection<?>) propertyValue, componentOrEntity);
              } catch (Exception ex) {
                LOG.error("An internal error occurred when forcing {} collection initialization.", propertyName);
                LOG.error("Source exception", ex);
              }
            }
          } else if (propertyValue instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) propertyValue;
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            if (li.getSession() != null && li.getSession().isOpen()) {
              try {
                Hibernate.initialize(propertyValue);
              } catch (Exception ex) {
                LOG.error("An internal error occurred when forcing {} reference initialization.", propertyName);
                LOG.error("Source exception", ex);
              }
            }
          }
        }

        if (!isInitialized(propertyValue)) {
          if (currentInitializationSession != null) {
            performPropertyInitializationUsingSession(componentOrEntity, propertyName, currentInitializationSession);
          } else {
            boolean suspendUnitOfWork = false;
            boolean startUnitOfWork = false;
            if (isSessionEntity) {
              // Always use NoTxSession to initialize session entities
              if (isUnitOfWorkActive) {
                suspendUnitOfWork = true;
              }
            } else {
              if (!isUnitOfWorkActive) {
                // Never use NoTxSession to initialize non session entities (see bug #1153)
                startUnitOfWork = true;
              }
            }
            try {
              if (suspendUnitOfWork) {
                suspendUnitOfWork();
              }
              if (startUnitOfWork) {
                getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
                  @Override
                  protected void doInTransactionWithoutResult(TransactionStatus status) {
                    initializeProperty(componentOrEntity, propertyName);
                    status.setRollbackOnly();
                  }
                });
              } else {
                initializeProperty(componentOrEntity, propertyName);
              }
            } finally {
              if (suspendUnitOfWork) {
                resumeUnitOfWork();
              }
            }
          }
        }
        componentOrEntity.firePropertyChange(propertyName, IPropertyChangeCapable.UNKNOWN, propertyValue);
      } finally {
        setDirtyTrackingEnabled(dirtRecorderWasEnabled);
      }
    }
  }

  private void initializeProperty(IComponent componentOrEntity, String propertyName) {
    Session hibernateSession = getHibernateSession();
    FlushMode oldFlushMode = hibernateSession.getFlushMode();
    try {
      // Temporary switch to a read-only session.
      hibernateSession.setFlushMode(FlushMode.MANUAL);
      try {
        currentInitializationSession = hibernateSession;
        performPropertyInitializationUsingSession(componentOrEntity, propertyName, hibernateSession);
      } finally {
        currentInitializationSession = null;
      }
    } finally {
      hibernateSession.setFlushMode(oldFlushMode);
    }
  }

  private boolean isSessionEntity(IComponent componentOrEntity) {
    return componentOrEntity instanceof IEntity && getRegisteredEntity(
        ((IEntity) componentOrEntity).getComponentContract(), ((IEntity) componentOrEntity).getId())
        == componentOrEntity;
  }

  @SuppressWarnings("unchecked")
  private void performPropertyInitializationUsingSession(final IComponent componentOrEntity, final String propertyName,
                                                         Session hibernateSession) {
    Object propertyValue = componentOrEntity.straightGetProperty(propertyName);
    if (!isInitialized(propertyValue)) {
      IEntityRegistry alreadyDetached = createEntityRegistry("detachFromHibernateInDepth");
      if (componentOrEntity instanceof IEntity) {
        if (((IEntity) componentOrEntity).isPersistent()) {
          detachFromHibernateInDepth(componentOrEntity, hibernateSession, alreadyDetached);
          lockInHibernate((IEntity) componentOrEntity, hibernateSession);
        } else if (IEntity.DELETED_VERSION.equals(((IEntity) componentOrEntity).getVersion())) {
          LOG.error("Trying to initialize a property ({}) on a deleted object ({} : {}).", propertyName,
              componentOrEntity.getComponentContract().getName(), componentOrEntity);
          throw new RuntimeException(
              "Trying to initialize a property (" + propertyName + ") on a deleted object (" + componentOrEntity
                  .getComponentContract().getName() + " : " + componentOrEntity + ")");
        } else if (propertyValue instanceof IEntity) {
          detachFromHibernateInDepth((IEntity) propertyValue, hibernateSession, alreadyDetached);
          lockInHibernate((IEntity) propertyValue, hibernateSession);
        }
      } else if (propertyValue instanceof IEntity) {
        // to handle initialization of component properties.
        detachFromHibernateInDepth((IEntity) propertyValue, hibernateSession, alreadyDetached);
        lockInHibernate((IEntity) propertyValue, hibernateSession);
      }

      if (propertyValue instanceof HibernateProxy) {
        HibernateProxy proxy = (HibernateProxy) propertyValue;
        LazyInitializer li = proxy.getHibernateLazyInitializer();
        if (li.getSession() == null) {
          try {
            li.setSession((SessionImplementor) hibernateSession);
          } catch (Exception ex) {
            LOG.error(
                "An internal error occurred when re-associating Hibernate session for {} reference initialization.",
                propertyName);
            LOG.error("Source exception", ex);
          }
        }
      } else if (propertyValue instanceof AbstractPersistentCollection) {
        AbstractPersistentCollection apc = (AbstractPersistentCollection) propertyValue;
        if (apc.getSession() == null) {
          try {
            apc.setCurrentSession((SessionImplementor) hibernateSession);
          } catch (Exception ex) {
            LOG.error(
                "An internal error occurred when re-associating Hibernate session for {} reference initialization.",
                propertyName);
            LOG.error("Source exception", ex);
          }
        }
      }
      Hibernate.initialize(propertyValue);
      if (propertyValue instanceof Collection<?> && propertyValue instanceof PersistentCollection) {
        relinkAfterInitialization((Collection<?>) propertyValue, componentOrEntity);
        for (Iterator<?> ite = ((Collection<?>) propertyValue).iterator(); ite.hasNext();) {
          Object collectionElement = ite.next();
          if (collectionElement instanceof IEntity) {
            if (isEntityRegisteredForDeletion((IEntity) collectionElement)) {
              ite.remove();
            }
          }
        }
      } else {
        relinkAfterInitialization(Collections.singleton(propertyValue), componentOrEntity);
      }
      clearPropertyDirtyState(propertyValue);
    }
  }

  private void relinkAfterInitialization(Collection<?> elements, Object owner) {
    for (Object element : elements) {
      // Should always be the case but there might be problems with lists
      // containing holes.
      if (element instanceof IComponent) {
        for (Map.Entry<String, Object> property : ((IComponent) element).straightGetProperties().entrySet()) {
          if (property.getValue() instanceof IEntity && owner instanceof IEntity) {
            if (owner != property.getValue() // avoid lazy initialization
                && ((IEntity) owner).getId().equals(((IEntity) property.getValue()).getId())
                // To avoid bug #548
                && Hibernate.getClass(owner) == Hibernate.getClass(property.getValue())) {
              ((IComponent) element).straightSetProperty(property.getKey(), owner);
            }
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
  @Override
  public void registerForDeletion(IEntity entity) {
    super.registerForDeletion(entity);
    if (isUnitOfWorkActive()) {
      Session hibernateSession = getHibernateSession();
      if (hibernateSession != getNoTxSession()) {
        // we are in an real tx
        hibernateSession.delete(entity);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForUpdate(IEntity entity) {
    super.registerForUpdate(entity);
    if (isUnitOfWorkActive()) {
      Session hibernateSession = getHibernateSession();
      if (hibernateSession != getNoTxSession()) {
        // we are in an real tx
        hibernateSession.saveOrUpdate(entity);
      }
    }
  }

  /**
   * This method wraps transient collections in the equivalent hibernate ones.
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected <E> Collection<E> wrapDetachedCollection(IEntity owner, Collection<E> detachedCollection,
                                                          Collection<E> snapshotCollection, String role) {
    Collection<E> varSnapshotCollection = snapshotCollection;
    if (!(detachedCollection instanceof PersistentCollection)) {
      String collectionRoleName = HibernateHelper.getHibernateRoleName(getComponentContract(owner), role);
      if (collectionRoleName == null) {
        // it is not an hibernate managed collection (e.g. "detachedEntities")
        return detachedCollection;
      }
      if (detachedCollection instanceof Set) {
        PersistentSet persistentSet = new PersistentSet(null, (Set<?>) detachedCollection);
        changeCollectionOwner(persistentSet, owner);
        HashMap<Object, Object> snapshot = new HashMap<>();
        if (varSnapshotCollection == null) {
          persistentSet.clearDirty();
          varSnapshotCollection = detachedCollection;
        }
        for (Object snapshotCollectionElement : varSnapshotCollection) {
          snapshot.put(snapshotCollectionElement, snapshotCollectionElement);
        }
        persistentSet.setSnapshot(owner.getId(), collectionRoleName, snapshot);
        return persistentSet;
      } else if (detachedCollection instanceof List) {
        PersistentList persistentList = new PersistentList(null, (List<?>) detachedCollection);
        changeCollectionOwner(persistentList, owner);
        ArrayList<Object> snapshot = new ArrayList<>();
        if (varSnapshotCollection == null) {
          persistentList.clearDirty();
          varSnapshotCollection = detachedCollection;
        }
        for (Object snapshotCollectionElement : varSnapshotCollection) {
          snapshot.add(snapshotCollectionElement);
        }
        persistentList.setSnapshot(owner.getId(), collectionRoleName, snapshot);
        return persistentList;
      }
    } else {
      if (varSnapshotCollection == null) {
        ((PersistentCollection) detachedCollection).clearDirty();
      } else {
        ((PersistentCollection) detachedCollection).dirty();
      }
    }
    return detachedCollection;
  }

  /**
   * Merge non initialized entity.
   *
   * @param <E>
   *     the actual entity type
   * @param entity
   *     the entity
   * @return the merged entity
   */
  @SuppressWarnings("unchecked")
  @Override
  protected <E extends IEntity> E mergeUninitializedEntity(E entity) {
    return (E) getNoTxSession().load(getComponentContract(entity), entity.getId());
  }

  /**
   * Merge collection.
   *
   * @param <E>
   *     the actual entity type.
   * @param propertyName
   *     the property name
   * @param propertyValue
   *     the property value
   * @param registeredEntity
   *     the registered entity
   * @param registeredCollection
   *     the registered collection
   * @return the collection
   */
  @Override
  @SuppressWarnings("unchecked")
  protected <E extends IEntity> Collection<?> mergeCollection(String propertyName, Object propertyValue,
                                                                       E registeredEntity,
                                                                       Collection<?> registeredCollection) {
    Collection<?> mergedCollection;
    if (propertyValue instanceof PersistentCollection) {
      Collection<?> snapshotCollection = null;
      Map<String, Object> dirtyProperties = getDirtyProperties(registeredEntity);
      if (dirtyProperties != null) {
        Object originalProperty = dirtyProperties.get(propertyName);
        // Workaround bug #1148
        if (originalProperty != null && originalProperty instanceof Collection<?>) {
          snapshotCollection = (Collection<?>) originalProperty;
        }
      }
      mergedCollection = wrapDetachedCollection(registeredEntity, ((Collection<Object>) registeredCollection),
          ((Collection<Object>) snapshotCollection), propertyName);
    } else {
      mergedCollection = registeredCollection;
    }
    return mergedCollection;
  }

  /**
   * Locks an entity (LockMode.NONE) in current hibernate session.
   *
   * @param entity
   *     the entity to lock.
   * @param hibernateSession
   *     the hibernate session.
   */
  private void lockInHibernate(IEntity entity, Session hibernateSession) {
    if (!hibernateSession.contains(entity)) {
      // Do not use get before trying to lock.
      // Get performs a DB query.
      try {
        hibernateSession.buildLockRequest(LockOptions.NONE).lock(entity);
      } catch (NonUniqueObjectException ex) {
        if (hibernateSession == noTxSession) {
          hibernateSession.clear();
          hibernateSession.buildLockRequest(LockOptions.NONE).lock(entity);
        } else {
          throw ex;
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void lockInHibernateInDepth(IComponent component, Session hibernateSession, IEntityRegistry alreadyLocked) {
    if (component == null) {
      return;
    }
    if (!isInitialized(component)) {
      lockInHibernate((IEntity) component, hibernateSession);
      return;
    }
    boolean isEntity = component instanceof IEntity;
    if (!isEntity || alreadyLocked.get(getComponentContract((IEntity) component), ((IEntity) component).getId())
        == null) {
      if (isEntity) {
        alreadyLocked.register(getComponentContract((IEntity) component), ((IEntity) component).getId(),
            (IEntity) component);
        if (((IEntity) component).isPersistent()) {
          lockInHibernate((IEntity) component, hibernateSession);
        }
        // else {
        // Cannot simply re-attach the transient entity, so we have to
        // saveOrUpdate it.

        // This is a bad evolution since we don't know if we want to actually
        // create the new entity. If we want to delete it, all checks will be
        // triggered.
        // if (!isEntityRegisteredForDeletion((IEntity) component)) {
        // registerForUpdate((IEntity) component);
        // }
        // }
      }
      Map<String, Object> entityProperties = component.straightGetProperties();
      IComponentDescriptor<?> componentDescriptor = getEntityFactory().getComponentDescriptor(getComponentContract(
          component));
      for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
        String propertyName = property.getKey();
        Object propertyValue = property.getValue();
        IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(propertyName);
        if (propertyValue instanceof IEntity) {
          lockInHibernateInDepth((IEntity) propertyValue, hibernateSession, alreadyLocked);
        } else if (propertyValue instanceof Collection && propertyDescriptor instanceof ICollectionPropertyDescriptor<?>
            && isInitialized(propertyValue)) {
          for (Object element : ((Collection<?>) property.getValue())) {
            if (element instanceof IComponent) {
              lockInHibernateInDepth((IComponent) element, hibernateSession, alreadyLocked);
            }
          }
          if (propertyValue instanceof PersistentCollection) {
            Collection<?> snapshot = null;
            Object storedSnapshot = ((PersistentCollection) propertyValue).getStoredSnapshot();
            if (storedSnapshot instanceof Map<?, ?>) {
              snapshot = ((Map<IComponent, IComponent>) storedSnapshot).keySet();
            } else if (storedSnapshot instanceof Collection<?>) {
              snapshot = (Collection<?>) storedSnapshot;
            }
            if (snapshot != null) {
              for (Object element : snapshot) {
                if (element instanceof IComponent) {
                  lockInHibernateInDepth((IComponent) element, hibernateSession, alreadyLocked);
                }
              }
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  void detachFromHibernateInDepth(IComponent component, Session hibernateSession, IEntityRegistry alreadyDetached) {
    if (component == null) {
      return;
    }
    boolean isEntity = component instanceof IEntity;
    // Always detach from Hibernate session. We might have already traversed the entity but not its Hibernate proxy.
    if (isEntity) {
      HibernateHelper.unsetProxyHibernateSession((IEntity) component, hibernateSession);
    }
    if (!isEntity || alreadyDetached.get(getComponentContract((IEntity) component), ((IEntity) component).getId())
        == null) {
      // Do not store uninitialized components since we may traverse initialized ones.
      if (isEntity && isInitialized(component)) {
        alreadyDetached.register(getComponentContract((IEntity) component), ((IEntity) component).getId(),
            (IEntity) component);
      }
      if (isInitialized(component)) {
        Map<String, Object> properties = component.straightGetProperties();
        for (Map.Entry<String, Object> property : properties.entrySet()) {
          Object propertyValue = property.getValue();
          if (propertyValue instanceof IComponent) {
            detachFromHibernateInDepth((IComponent) propertyValue, hibernateSession, alreadyDetached);
          } else if (propertyValue instanceof PersistentCollection) {
            HibernateHelper.unsetCollectionHibernateSession((PersistentCollection) propertyValue, hibernateSession);
            if (propertyValue instanceof Collection<?> && isInitialized(propertyValue)) {
              for (Object element : ((Collection<?>) propertyValue)) {
                if (element instanceof IComponent) {
                  detachFromHibernateInDepth((IComponent) element, hibernateSession, alreadyDetached);
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * Finds an entity by ID.
   *
   * @param <T>
   *     the entity type to return
   * @param id
   *     the entity ID.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the found entity
   */
  @SuppressWarnings("unchecked")
  public <T extends IEntity> T findById(final Serializable id, final EMergeMode mergeMode,
                                        final Class<? extends T> clazz) {
    T res;
    if (isUnitOfWorkActive()) {
      // merge mode must be ignored if a transaction is pre-existing.
      res = cloneInUnitOfWork((T) getHibernateSession().get(clazz, id));
    } else {
      // merge mode is used for merge to occur inside the transaction.
      res = getTransactionTemplate().execute(new TransactionCallback<T>() {

        @SuppressWarnings("unchecked")
        @Override
        public T doInTransaction(TransactionStatus status) {
          return merge((T) getHibernateSession().get(clazz, id), mergeMode);
        }
      });
    }
    return res;
  }

  /**
   * Search Hibernate using criteria. The result is then merged into session unless the method is called into a
   * pre-existing transaction, in which case, the merge mode is ignored and the merge is not performed.
   *
   * @param <T>
   *     the entity type to return
   * @param criteria
   *     the detached criteria.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the first found entity or null
   */
  public <T extends IEntity> T findFirstByCriteria(DetachedCriteria criteria, EMergeMode mergeMode,
                                                   Class<? extends T> clazz) {
    List<T> ret = findByCriteria(criteria, 0, 1, mergeMode, clazz);
    if (ret != null && !ret.isEmpty()) {
      return ret.get(0);
    }
    return null;
  }

  /**
   * Search Hibernate using criteria. The result is then merged into session unless the method is called into a
   * pre-existing transaction, in which case, the merge mode is ignored and the merge is not performed.
   *
   * @param <T>
   *     the entity type to return
   * @param criteria
   *     the detached criteria.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the first found entity or null
   */
  public <T extends IEntity> List<T> findByCriteria(final DetachedCriteria criteria, EMergeMode mergeMode,
                                                    Class<? extends T> clazz) {
    return findByCriteria(criteria, -1, -1, mergeMode, clazz);
  }

  /**
   * Search Hibernate using criteria. The result is then merged into session unless the method is called into a
   * pre-existing transaction, in which case, the merge mode is ignored and the merge is not performed.
   *
   * @param <T>
   *     the entity type to return
   * @param criteria
   *     the detached criteria.
   * @param firstResult
   *     the first result rank to retrieve.
   * @param maxResults
   *     the max number of results to retrieve.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the first found entity or null
   */
  @SuppressWarnings("UnusedParameters")
  public <T extends IEntity> List<T> findByCriteria(final DetachedCriteria criteria, int firstResult, int maxResults,
                                                    EMergeMode mergeMode, Class<? extends T> clazz) {
    List<T> res;
    if (isUnitOfWorkActive()) {
      // merge mode must be ignored if a transaction is pre-existing, so force
      // to null.

      // This is useless to clone in UOW now that UOW registration is done
      // in onLoad interceptor
      // res = (List<T>) cloneInUnitOfWork(find(criteria, firstResult,
      // maxResults, null));

      res = find(criteria, firstResult, maxResults, null);
    } else {
      // merge mode is passed for merge to occur inside the transaction.
      res = find(criteria, firstResult, maxResults, mergeMode);
    }
    return res;
  }

  private <T extends IEntity> List<T> find(final DetachedCriteria criteria, final int firstResult, final int maxResults,
                                           final EMergeMode mergeMode) {
    List<T> entities =  getTransactionTemplate().execute(new TransactionCallback<List<T>>() {

      @SuppressWarnings("unchecked")
      @Override
      public List<T> doInTransaction(TransactionStatus status) {
        Criteria executableCriteria = criteria.getExecutableCriteria(getHibernateSession());
        if (firstResult >= 0) {
          executableCriteria.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
          executableCriteria.setMaxResults(maxResults);
        }
        List<T> entities = executableCriteria.list();
        if (mergeMode != null) {
          entities = merge(entities, mergeMode);
        }
        return entities;
      }
    });
    if (isUnitOfWorkActive()) {
      entities = cloneInUnitOfWork(entities);
    }
    return entities;
  }

  /**
   * Unwrap hibernate proxy if needed.
   *
   * @param componentOrProxy
   *     the component or proxy.
   * @return the proxy implementation if it's an hibernate proxy.
   */
  @Override
  protected Object unwrapProxy(Object componentOrProxy) {
    Object component;
    if (componentOrProxy instanceof HibernateProxy) {
      // we must unwrap the proxy to avoid class cast exceptions.
      // see
      // http://forum.hibernate.org/viewtopic.php?p=2323464&sid=cb4ba3a4418276e5d2fbdd6c906ba734
      component = ((HibernateProxy) componentOrProxy).getHibernateLazyInitializer().getImplementation();
    } else {
      component = componentOrProxy;
    }
    return component;
  }

  /**
   * Reloads an entity in Hibernate.
   *
   * @param entity
   *     the entity to reload.
   */
  @Override
  public void reload(final IEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Passed entity cannot be null");
    }
    // builds a collection of entities to reload.
    Set<IEntity> dirtyReachableEntities = buildReachableDirtyEntitySet(entity);

    if (entity.isPersistent()) {
      getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {

          Exception deletedObjectEx = null;
          try {
            merge((IEntity) getHibernateSession().load(getComponentContract(entity).getName(), entity.getId()),
                EMergeMode.MERGE_CLEAN_EAGER);
          } catch (ObjectNotFoundException ex) {
            deletedObjectEx = ex;
          }
          // if reloadObject is part of a pre-existing transaction, do not rollback the TX
          // status.setRollbackOnly();
          if (deletedObjectEx != null) {
            throw new ConcurrencyFailureException(deletedObjectEx.getMessage(), deletedObjectEx);
          }
        }
      });
    }

    // traverse the reachable dirty entities to explicitly reload the
    // ones that were not reloaded by the previous pass.
    for (IEntity reachableEntity : dirtyReachableEntities) {
      if (reachableEntity.isPersistent() && isDirty(reachableEntity)) {
        reload(reachableEntity);
      }
    }
  }

  private Set<IEntity> buildReachableDirtyEntitySet(IEntity entity) {
    Set<IEntity> reachableDirtyEntities = new HashSet<>();
    completeReachableDirtyEntities(entity, reachableDirtyEntities, new HashSet<IEntity>());
    return reachableDirtyEntities;
  }

  private void completeReachableDirtyEntities(IEntity entity, Set<IEntity> reachableDirtyEntities,
                                              Set<IEntity> alreadyTraversed) {
    if (alreadyTraversed.contains(entity)) {
      return;
    }
    alreadyTraversed.add(entity);
    if (isDirty(entity)) {
      reachableDirtyEntities.add(entity);
    }
    Map<String, Object> entityProps = entity.straightGetProperties();
    IComponentDescriptor<?> entityDescriptor = getEntityFactory().getComponentDescriptor(getComponentContract(entity));
    for (Map.Entry<String, Object> property : entityProps.entrySet()) {
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        IPropertyDescriptor propertyDescriptor = entityDescriptor.getPropertyDescriptor(property.getKey());
        if (isInitialized(propertyValue) && propertyDescriptor instanceof IRelationshipEndPropertyDescriptor
            // It's not a master data relationship.
            && ((IRelationshipEndPropertyDescriptor) propertyDescriptor).getReverseRelationEnd() != null) {
          completeReachableDirtyEntities((IEntity) propertyValue, reachableDirtyEntities, alreadyTraversed);
        }
      } else if (propertyValue instanceof Collection<?>) {
        if (isInitialized(propertyValue)) {
          for (Object elt : ((Collection<?>) propertyValue)) {
            if (elt instanceof IEntity) {
              completeReachableDirtyEntities((IEntity) elt, reachableDirtyEntities, alreadyTraversed);
            }
          }
        }
      }
    }
  }

  private void changeCollectionOwner(Collection<?> persistentCollection, Object newOwner) {
    if (persistentCollection instanceof PersistentCollection) {
      ((PersistentCollection) persistentCollection).setOwner(newOwner);
    }
  }

  /**
   * Hibernate related cloning.
   * <p/>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected <E> E cloneUninitializedProperty(Object owner, E propertyValue) {
    E clonedPropertyValue = propertyValue;
    if (isInitialized(owner)) {
      if (propertyValue instanceof PersistentCollection) {
        if (unwrapProxy((((PersistentCollection) propertyValue).getOwner())) != unwrapProxy(owner)) {
          if (propertyValue instanceof PersistentSet) {
            clonedPropertyValue = (E) new PersistentSet(
                // Must reset the session.
                // See bug #902
                /* ((PersistentSet) propertyValue).getSession() */null);
          } else if (propertyValue instanceof PersistentList) {
            clonedPropertyValue = (E) new PersistentList(
                // Must reset the session.
                // See bug #902
                /* ((PersistentList) propertyValue).getSession() */null);
          }
          changeCollectionOwner((Collection<?>) clonedPropertyValue, owner);
          ((PersistentCollection) clonedPropertyValue).setSnapshot(((PersistentCollection) propertyValue).getKey(),
              ((PersistentCollection) propertyValue).getRole(), null);
        }
      } else {
        if (propertyValue instanceof HibernateProxy) {
          return (E) getHibernateSession().load(
              ((HibernateProxy) propertyValue).getHibernateLazyInitializer().getEntityName(),
              ((IEntity) propertyValue).getId());
        }
      }
    }
    return clonedPropertyValue;
  }

  /**
   * Clears dirty state of persistent collections.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected void clearPropertyDirtyState(Object property) {
    if (property instanceof PersistentCollection) {
      ((PersistentCollection) property).clearDirty();
    }
  }

  /**
   * Sets the hibernateSessionFactory.
   *
   * @param hibernateSessionFactory
   *     the hibernateSessionFactory to set.
   */
  public void setHibernateSessionFactory(SessionFactory hibernateSessionFactory) {
    this.hibernateSessionFactory = hibernateSessionFactory;
  }

  /**
   * Gets the hibernateSessionFactory.
   *
   * @return the hibernateSessionFactory.
   */
  protected SessionFactory getHibernateSessionFactory() {
    return hibernateSessionFactory;
  }

  /**
   * Gets the defaultTxFlushMode.
   *
   * @return the defaultTxFlushMode.
   */
  protected FlushMode getDefaultTxFlushMode() {
    return defaultTxFlushMode;
  }

  /**
   * Sets the default Hibernate flush mode to apply to the Hibernate session
   * when it is bound to a transaction. Defaults to {@link FlushMode#COMMIT}.
   *
   * @param defaultTxFlushMode
   *     the defaultTxFlushMode to set.
   */
  public void setDefaultTxFlushMode(String defaultTxFlushMode) {
    this.defaultTxFlushMode = FlushMode.valueOf(defaultTxFlushMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cleanupRequestResources() {
    super.cleanupRequestResources();
    if (noTxSession != null) {
      if (noTxSession.isOpen()) {
        Connection conn = noTxSession.close();
        if (conn != null) {
          try {
            conn.close();
          } catch (SQLException ex) {
            LOG.warn("The provided non transactional connection could not be correctly closed.", ex);
          }
        }
      }
      noTxSession = null;
    }
  }

  /**
   * Checks also for Hibernate proxies.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected boolean objectEquals(IEntity e1, IEntity e2) {
    return HibernateHelper.objectEquals(e1, e2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected <E extends IComponent> Class<? extends E> getComponentContract(E component) {
    return HibernateHelper.getComponentContract(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IEntityRegistry createEntityRegistry(String name,
                                                 Map<Class<? extends IEntity>, Map<Serializable, IEntity>> backingStore) {
    return new HibernateEntityRegistry(name, backingStore);
  }

  /**
   * Configures the entity factory to use to create new entities. Backend
   * controllers only accept instances of
   * {@code HibernateControllerAwareProxyEntityFactory} or a subclass.
   *
   * @param entityFactory
   *     the entityFactory to set.
   */
  @Override
  public void setEntityFactory(IEntityFactory entityFactory) {
    if (!(entityFactory instanceof HibernateControllerAwareProxyEntityFactory)) {
      throw new IllegalArgumentException(
          "entityFactory must be a " + HibernateControllerAwareProxyEntityFactory.class.getSimpleName());
    }
    super.setEntityFactory(entityFactory);
  }

  /**
   * Sets the noTxDataSource.
   *
   * @param noTxDataSource
   *     the noTxDataSource to set.
   */
  public void setNoTxDataSource(DataSource noTxDataSource) {
    this.noTxDataSource = noTxDataSource;
  }

  /**
   * Flushes the current underlying hibernate session.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void flush() {
    getHibernateSession().flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterCompletion(int status) {
    super.afterCompletion(status);
    if (getTransactionTemplate().getTransactionManager() instanceof JtaTransactionManager) {
      TransactionSynchronizationManager.unbindResourceIfPossible(getHibernateSessionFactory());
    }
    // This is to avoid having entities attached to 2 open sessions
    // and to periodically clear the noTxSession cache.
    if (noTxSession != null) {
      noTxSession.clear();
    }
  }

}
