/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.io.Serializable;

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
   * @param entityContract
   *          the class of the entity to create.
   * @return the entity instance.
   */
  IEntity createEntityInstance(Class entityContract);

  /**
   * Creates a new entity instance based on the entity descriptor.
   * 
   * @param entityContract
   *          the class of the entity to create.
   * @param id
   *          the identifier to set on the entity.
   * @return the entity instance.
   */
  IEntity createEntityInstance(Class entityContract, Serializable id);

  /**
   * Creates a new query entity instance based on the entity descriptor.
   * 
   * @param entityContract
   *          the class of the entity to create.
   * @return the query entity instance.
   */
  IQueryEntity createQueryEntityInstance(Class entityContract);
}
