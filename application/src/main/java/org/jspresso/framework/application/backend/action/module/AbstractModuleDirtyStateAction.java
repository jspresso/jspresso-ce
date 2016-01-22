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
package org.jspresso.framework.application.backend.action.module;

import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.Module;

/**
 * This is the base abstract class for actions that are responsible for checking
 * module dirty state. <i>Dirty</i> is taken in the sense of an entity needing
 * to be flushed to the persistent store. Among modules that are to be checked,
 * a collection module is marked dirty if and only if one of its module objects
 * is an entity which is dirty. On the other hand, a bean module is marked dirty
 * if and only if its (single) module object is an entity and is dirty.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class AbstractModuleDirtyStateAction<E, F, G> extends
    FrontendAction<E, F, G> {

  /**
   * Checks dirty state of all the modules content and informs the user if state
   * is dirty.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    Collection<Module> modulesToCheck = getModulesToCheck(context);
    if (modulesToCheck != null) {
      for (Module module : modulesToCheck) {
        if (module != null) {
          module.refreshDirtinessInDepth(getController(context).getBackendController());
        }
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the collection of modules to check the dirty state for.
   *
   * @param context
   *          the action context.
   * @return the collection of modules to check the dirty state for.
   */
  protected abstract Collection<Module> getModulesToCheck(
      Map<String, Object> context);
}
