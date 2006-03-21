/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.service;

/**
 * Defines all the entity lifecycle hooks.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the class of the intercepted entity.
 */
public interface ILifecycleInterceptor<E> {

  /**
   * Called whenever an entity is created in memory.
   * 
   * @param entity
   *          the created entity.
   * @return true if the state of the entity has been updated.
   */
  boolean onCreate(E entity);

  /**
   * Called whenever an entity is made persistent for the first time.
   * 
   * @param entity
   *          the persisted entity.
   * @return true if the state of the entity has been updated.
   */
  boolean onPersist(E entity);

  /**
   * Called whenever an persistent entity is updated.
   * 
   * @param entity
   *          the updated entity.
   * @return true if the state of the entity has been updated.
   */
  boolean onUpdate(E entity);

  /**
   * Called whenever an persistent entity is deleted.
   * 
   * @param entity
   *          the deleted entity.
   * @return true if the state of the entity has been updated.
   */
  boolean onDelete(E entity);
}
