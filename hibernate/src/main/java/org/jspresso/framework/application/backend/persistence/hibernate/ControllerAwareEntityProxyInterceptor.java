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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Transaction;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.hibernate.type.Type;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.model.persistence.hibernate.EntityProxyInterceptor;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Hibernate session interceptor aware of a backend controller to deal with
 * uniqueness of entity instances across the JVM.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerAwareEntityProxyInterceptor extends
    EntityProxyInterceptor {

  private static final long  serialVersionUID = -6834992000307471098L;

  private IBackendController backendController;

  /**
   * Begins the backend controller current unit of work.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void afterTransactionBegin(Transaction tx) {
    backendController.beginUnitOfWork();
    super.afterTransactionBegin(tx);
  }

  /**
   * Either commits or rollbacks the backend controller current unit of work.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void afterTransactionCompletion(Transaction tx) {
    if (tx.wasCommitted()) {
      backendController.commitUnitOfWork();
    } else {
      backendController.rollbackUnitOfWork();
    }
    super.afterTransactionCompletion(tx);
  }

  /**
   * Uses the backend controller to retrieve the dirty properties of the entity.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public int[] findDirty(Object entity, Serializable id, Object[] currentState,
      Object[] previousState, String[] propertyNames, Type[] types) {
    if (entity instanceof IEntity) {
      Map<String, Object> dirtyProperties = backendController
          .getDirtyProperties((IEntity) entity);
      if (dirtyProperties != null) {
        dirtyProperties.remove(IEntity.VERSION);
      }
      if (dirtyProperties == null) {
        return null;
      } else if (dirtyProperties.isEmpty()) {
        return new int[0];
      }
      if (dirtyProperties.containsKey(IEntity.ID)) {
        // whenever an entity has just been saved, its state is in the dirty
        // store.
        // hibernate might ask to check dirtyness especially for collection
        // members.
        // Those just saved entities must not be considered dirty.
        return new int[0];
      }
      int[] indices = new int[dirtyProperties.size()];
      int n = 0;
      for (int i = 0; i < propertyNames.length; i++) {
        if (dirtyProperties.containsKey(propertyNames[i])) {
          indices[n] = i;
          n++;
        }
      }
      int[] shrinkedArray = new int[n];
      System.arraycopy(indices, 0, shrinkedArray, 0, n);
      return shrinkedArray;
    }
    return super.findDirty(entity, id, currentState, previousState,
        propertyNames, types);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object getEntity(String entityName, Serializable id) {
    if (!backendController.isUnitOfWorkActive()) {
      try {
        IEntity registeredEntity = backendController.getRegisteredEntity(
            (Class<? extends IEntity>) Class.forName(entityName), id);
        if (registeredEntity instanceof HibernateProxy) {
          HibernateProxy proxy = (HibernateProxy) registeredEntity;
          LazyInitializer li = proxy.getHibernateLazyInitializer();
          registeredEntity = (IEntity) li.getImplementation();
        }

        HibernateBackendController
            .cleanPersistentCollectionDirtyState(registeredEntity);
        return registeredEntity;
      } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
      }
    }
    return super.getEntity(entityName, id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onLoad(Object entity, Serializable id, Object[] state,
      String[] propertyNames, Type[] types) {
    if (!backendController.isUnitOfWorkActive()) {
      if (entity instanceof IEntity
          && backendController.getRegisteredEntity(((IEntity) entity)
              .getComponentContract(), id) == null) {
        Map<String, Object> properties = new HashMap<String, Object>();
        for (int i = 0; i < propertyNames.length; i++) {
          if (state[i] != null) {
            properties.put(propertyNames[i], state[i]);
          }
        }
        ((IEntity) entity).straightSetProperties(properties);
        backendController.registerEntity((IEntity) entity, false);
      }
    }
    return super.onLoad(entity, id, state, propertyNames, types);
  }

  /**
   * registers Enitities to be merged back from the uow to the session on
   * commmit.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void postFlush(Iterator entities) {
    while (entities.hasNext()) {
      Object entity = entities.next();
      if (entity instanceof IEntity) {
        backendController.recordAsSynchronized((IEntity) entity);
      }
    }
    super.postFlush(entities);
  }

  /**
   * Sets the backendController.
   * 
   * @param backendController
   *          the backendController to set.
   */
  public void setBackendController(IBackendController backendController) {
    this.backendController = backendController;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return backendController;
  }

  /**
   * Gets the principal of the application session.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected UserPrincipal getPrincipal() {
    return backendController.getApplicationSession().getPrincipal();
  }

  /**
   * This is the place to trigger the update lifecycle handler. onFlushDirty is
   * not the right place since it cannot deal with transient new instances that
   * might be added to the object tree.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void preFlush(Iterator entities) {
    // To avoid concurrent access modifications
    Set<Object> cloneSet = new LinkedHashSet<Object>();
    while (entities.hasNext()) {
      cloneSet.add(entities.next());
    }
    for (Object entity : cloneSet) {
      if (entity instanceof IEntity && ((IEntity) entity).isPersistent()) {
        boolean isClean = false;
        Map<String, Object> dirtyProperties = backendController
            .getDirtyProperties((IEntity) entity);
        if (dirtyProperties != null) {
          dirtyProperties.remove(IEntity.VERSION);
        }
        if (dirtyProperties == null) {
          isClean = true;
        } else if (dirtyProperties.isEmpty()) {
          isClean = true;
        } else if (dirtyProperties.containsKey(IEntity.ID)) {
          // whenever an entity has just been saved, its state is in the dirty
          // store.
          // hibernate might ask to check dirtyness especially for collection
          // members.
          // Those just saved entities must not be considered dirty.
          isClean = true;
        } else {
          isClean = false;
        }
        if (!isClean) {
          // the entity is dirty and is going to be flushed.
          ((IEntity) entity).onUpdate(getEntityFactory(), getPrincipal(),
              getEntityLifecycleHandler());
        }
      }
    }
  }
}
