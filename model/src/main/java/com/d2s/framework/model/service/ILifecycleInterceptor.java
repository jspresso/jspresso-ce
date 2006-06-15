/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.service;

import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.security.UserPrincipal;

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
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onCreate(E entity, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an entity is made persistent for the first time.
   * 
   * @param entity
   *          the persisted entity.
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onPersist(E entity, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an persistent entity is updated.
   * 
   * @param entity
   *          the updated entity.
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onUpdate(E entity, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an persistent entity is deleted.
   * 
   * @param entity
   *          the deleted entity.
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onDelete(E entity, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);
}
