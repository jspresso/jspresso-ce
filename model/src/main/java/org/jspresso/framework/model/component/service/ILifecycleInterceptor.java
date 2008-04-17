/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component.service;

import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;

import com.d2s.framework.security.UserPrincipal;

/**
 * Defines all the components lifecycle hooks.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the class of the intercepted entity.
 */
public interface ILifecycleInterceptor<E> {

  /**
   * Called whenever an entity is created in memory.
   * 
   * @param component
   *            the created entity.
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onCreate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an persistent entity is deleted.
   * 
   * @param component
   *            the deleted entity.
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onDelete(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an entity is made persistent for the first time.
   * 
   * @param component
   *            the persisted entity.
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onPersist(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an persistent entity is updated.
   * 
   * @param component
   *            the updated entity.
   * @param entityFactory
   *            an entity factory instance which can be used to complete the
   *            lifecycle step.
   * @param principal
   *            the principal triggering the action.
   * @param entityLifecycleHandler
   *            entityLifecycleHandler.
   * @return true if the state of the entity has been updated.
   */
  boolean onUpdate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);
}
