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
package org.jspresso.framework.model.component;

import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Defines the component lifecycle hooks.
 *
 * @author Vincent Vandenschrick
 */
public interface ILifecycleCapable {

  /**
   * {@code ON_CREATE_METHOD_NAME}.
   */
  String ON_CREATE_METHOD_NAME  = "onCreate";

  /**
   * {@code ON_DELETE_METHOD_NAME}.
   */
  String ON_DELETE_METHOD_NAME  = "onDelete";

  /**
   * {@code ON_LOAD_METHOD_NAME}.
   */
  String ON_LOAD_METHOD_NAME    = "onLoad";

  /**
   * {@code ON_CLONE_METHOD_NAME}.
   */
  String ON_CLONE_METHOD_NAME   = "onClone";

  /**
   * {@code ON_PERSIST_METHOD_NAME}.
   */
  String ON_PERSIST_METHOD_NAME = "onPersist";

  /**
   * {@code ON_UPDATE_METHOD_NAME}.
   */
  String ON_UPDATE_METHOD_NAME  = "onUpdate";

  /**
   * Called when an component is created (still transient).
   *
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onCreate(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an component is deleted (delete).
   *
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onDelete(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called when an component is loaded from the persistent store or merged back
   * from the unit of work. The component state is fully initialized when this
   * method is called. The onLoad callback may be used to perform some extra
   * technical initializations like registering some listeners.
   */
  void onLoad();

  /**
   * Called whenever an entity is cloned to the unit of work. The component
   * state is fully initialized when this method is called. The onClone callback
   * may be used to perform some extra technical initializations like
   * registering some listeners or initializing some non persistent properties.
   *
   * @param <E>
   *          tha actual component type.
   * @param sourceComponent
   *          the component that is the source of the cloning.
   */
  <E extends IComponent> void onClone(E sourceComponent);

  /**
   * Called just before an component is persisted (insert).
   *
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onPersist(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);

  /**
   * Called just before an component is updated (update).
   *
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  boolean onUpdate(IEntityFactory entityFactory, UserPrincipal principal,
      IEntityLifecycleHandler entityLifecycleHandler);
}
