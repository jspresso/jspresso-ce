/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.io.Serializable;

import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;

/**
 * This interface defines the contract of an entities factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityFactory {

  /**
   * Creates a new entity instance based on the entity descriptor. The entity
   * will be initialized with any necessary starting state.
   * 
   * @param <T>
   *          the concrete class of the created entity.
   * @param entityContract
   *          the class of the entity to create.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract);

  /**
   * Creates a new entity instance based on the entity descriptor.
   * 
   * @param <T>
   *          the concrete class of the created entity.
   * @param entityContract
   *          the class of the entity to create.
   * @param id
   *          the identifier to set on the entity.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id);

  /**
   * Creates a new query entity instance based on the entity descriptor.
   * 
   * @param <T>
   *          the concrete class of the created query entity.
   * @param entityContract
   *          the class of the entity to create.
   * @return the query entity instance.
   */
  <T extends IQueryEntity> T createQueryEntityInstance(Class<T> entityContract);

  /**
   * Gets an entity descriptor from its conract.
   * 
   * @param <T>
   *          the entity type.
   * @param entityContract
   *          the entity contract
   * @return the entity descriptor.
   */
  <T extends IEntity> IEntityDescriptor getEntityDescriptor(
      Class<T> entityContract);
}
