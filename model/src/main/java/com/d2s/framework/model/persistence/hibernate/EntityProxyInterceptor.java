/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;

import com.d2s.framework.model.entity.EntityException;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;

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

  /**
   * Instanciates a new entity (proxy) based on the entityDescriptor returned
   * from the bean factory. The entityName which is the fully qualified name of
   * the entity interface contract is used as the lookup key in the bean
   * factory.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unused")
  public Object instantiate(String entityName, EntityMode entityMode,
      Serializable id) {
    try {
      return entityFactory.createEntityInstance(Class.forName(entityName), id);
    } catch (ClassNotFoundException ex) {
      throw new EntityException(ex);
    }
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
   * Sets the entityFactory.
   * 
   * @param entityFactory
   *          the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }
}
