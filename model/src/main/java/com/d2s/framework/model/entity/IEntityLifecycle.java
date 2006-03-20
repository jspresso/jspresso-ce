/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

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
   */
  void onCreate();

  /**
   * Called just before an entity is persisted (insert).
   */
  void onPersist();

  /**
   * Called just before an entity is updated (update).
   */
  void onUpdate();

  /**
   * Called just before an entity is deleted (delete).
   */
  void onDelete();
}
