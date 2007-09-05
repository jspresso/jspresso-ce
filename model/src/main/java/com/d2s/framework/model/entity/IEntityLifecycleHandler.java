/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

/**
 * Defaines the contract of any component able to handle entities lifecycle.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityLifecycleHandler {

  /**
   * Registers an entity for deletion.
   * 
   * @param entity
   *          the entity to register.
   */
  void deleteEntity(IEntity entity);

  /**
   * Tests wether an entity has been registered for deletion.
   * 
   * @param entity
   *          the entity to test.
   * @return true if the entity has been registered for deletion.
   */
  boolean isEntityRegisteredForDeletion(IEntity entity);

  /**
   * Tests wether an entity has been registered for update.
   * 
   * @param entity
   *          the entity to test.
   * @return true if the entity has been registered for update.
   */
  boolean isEntityRegisteredForUpdate(IEntity entity);

  /**
   * Registers an entity for update.
   * 
   * @param entity
   *          the entity to register.
   */
  void updateEntity(IEntity entity);
}
