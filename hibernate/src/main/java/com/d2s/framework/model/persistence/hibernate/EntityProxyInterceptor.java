/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.type.Type;

import com.d2s.framework.model.entity.EntityException;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.security.UserPrincipal;

/**
 * This hibernate interceptor enables hibernate to handle entities which are
 * implemented by proxies. Its main goal is to provide hibernate with new
 * instances of entities based on their core interface contract.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityProxyInterceptor extends EmptyInterceptor {

  private static final long serialVersionUID = -7357726538191018694L;

  private IEntityFactory    entityFactory;

  private void extractState(IEntity entity, String[] propertyNames,
      Object[] state) {
    for (int i = 0; i < propertyNames.length; i++) {
      String propertyName = propertyNames[i];
      Object property = entity.straightGetProperty(propertyName);
      if (!(property instanceof Collection<?>)) {
        state[i] = property;
      }
    }
  }

  /**
   * Gets the entityFactory.
   *
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Gets the lifecycle handler.
   *
   * @return the entity lifecycle handler.
   */
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return null;
  }

  /**
   * Returns the fully qualified name of the entity passed as parameter.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getEntityName(Object object) {
    if (object instanceof IEntity) {
      return ((IEntity) object).getContract().getName();
    }
    return null;
  }

  /**
   * Gets the principal owning the session.
   *
   * @return the principal owning the session.
   */
  protected UserPrincipal getPrincipal() {
    return null;
  }

  /**
   * Instanciates a new entity (proxy) based on the entityDescriptor returned
   * from the bean factory. The entityName which is the fully qualified name of
   * the entity interface contract is used as the lookup key in the bean
   * factory.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings({"unused", "unchecked" })
  public Object instantiate(String entityName, EntityMode entityMode,
      Serializable id) {
    try {
      return entityFactory.createEntityInstance(
          (Class<? extends IEntity>) Class.forName(entityName), id);
    } catch (ClassNotFoundException ex) {
      throw new EntityException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onDelete(Object entity, Serializable id, Object[] state,
      String[] propertyNames, Type[] types) {
    if (entity instanceof IEntity) {
      ((IEntity) entity).onDelete(getEntityFactory(), getPrincipal(),
          getEntityLifecycleHandler());
    }
    super.onDelete(entity, id, state, propertyNames, types);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onFlushDirty(Object entity, Serializable id,
      Object[] currentState, Object[] previousState, String[] propertyNames,
      Type[] types) {
    boolean stateUpdated = false;
    if (entity instanceof IEntity) {
      if (((IEntity) entity).onUpdate(getEntityFactory(), getPrincipal(),
          getEntityLifecycleHandler())) {
        extractState((IEntity) entity, propertyNames, currentState);
        stateUpdated = true;
      }
    }
    return stateUpdated
        || super.onFlushDirty(entity, id, currentState, previousState,
            propertyNames, types);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state,
      String[] propertyNames, Type[] types) {
    boolean stateUpdated = false;
    if (entity instanceof IEntity) {
      if (((IEntity) entity).onPersist(getEntityFactory(), getPrincipal(),
          getEntityLifecycleHandler())) {
        extractState((IEntity) entity, propertyNames, state);
        stateUpdated = true;
      }
    }
    return stateUpdated
        || super.onSave(entity, id, state, propertyNames, types);
  }

  /**
   * Sets the entityFactory.
   *
   * @param entityFactory
   *          the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }
}
