/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.model.component.service;

import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Defines all the components lifecycle hooks.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the class of the intercepted entity.
 */
@SuppressWarnings({"EmptyMethod", "UnusedReturnValue", "UnusedParameters"})
public interface ILifecycleInterceptor<E> {

  /**
   * Called whenever an entity is created in memory.
   *
   * @param component
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
  boolean onCreate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an persistent entity is deleted.
   *
   * @param component
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
  boolean onDelete(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an entity is loaded from the persistent store or merged
   * back from the unit of work. The component state is fully initialized when
   * this method is called. The onLoad callback may be used to perform some
   * extra technical initializations like registering some listeners.
   *
   * @param component
   *          the created entity.
   */
  void onLoad(E component);

  /**
   * Called whenever an entity is cloned to the unit of work. The component
   * state is fully initialized when this method is called. The onClone callback
   * may be used to perform some extra technical initializations like
   * registering some listeners or initializing some non persistent properties.
   *
   * @param component
   *          the entity clone.
   * @param sourceComponent
   *          the source of the cloned entity.
   */
  void onClone(E component, E sourceComponent);

  /**
   * Called whenever an entity is made persistent for the first time.
   *
   * @param component
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
  boolean onPersist(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called whenever an persistent entity is updated.
   *
   * @param component
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
  boolean onUpdate(E component, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);
}
