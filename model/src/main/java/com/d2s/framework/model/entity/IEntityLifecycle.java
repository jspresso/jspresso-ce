/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import com.d2s.framework.security.UserPrincipal;

/**
 * Defines the entity lifecycle hooks.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityLifecycle {

  /**
   * Called when an entity is created (still transient).
   * 
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onCreate(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an entity is persisted (insert).
   * 
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onPersist(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an entity is updated (update).
   * 
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onUpdate(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an entity is deleted (delete).
   * 
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onDelete(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);
}
