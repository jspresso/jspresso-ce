/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component;

import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;

import com.d2s.framework.security.UserPrincipal;

/**
 * Defines the component lifecycle hooks.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ILifecycleCapable {

  /**
   * <code>ON_CREATE_METHOD_NAME</code>.
   */
  String ON_CREATE_METHOD_NAME  = "onCreate";

  /**
   * <code>ON_DELETE_METHOD_NAME</code>.
   */
  String ON_DELETE_METHOD_NAME  = "onDelete";

  /**
   * <code>ON_PERSIST_METHOD_NAME</code>.
   */
  String ON_PERSIST_METHOD_NAME = "onPersist";

  /**
   * <code>ON_UPDATE_METHOD_NAME</code>.
   */
  String ON_UPDATE_METHOD_NAME  = "onUpdate";

  /**
   * Called when an component is created (still transient).
   * 
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onCreate(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an component is deleted (delete).
   * 
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onDelete(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an component is persisted (insert).
   * 
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onPersist(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an component is updated (update).
   * 
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onUpdate(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);
}
