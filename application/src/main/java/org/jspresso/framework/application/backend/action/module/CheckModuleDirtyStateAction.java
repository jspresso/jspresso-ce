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
import java.util.Collections;
import java.util.Map;

import org.jspresso.framework.application.model.Module;

/**
 * This action recomputes the dirty state of the current selected module. It is
 * typically triggered when the user navigates (leaves) out of the module to
 * compute a visual notification of a pending change.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class CheckModuleDirtyStateAction<E, F, G> extends
    AbstractModuleDirtyStateAction<E, F, G> {

  /**
   * Returns current module.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Collection<Module> getModulesToCheck(Map<String, Object> context) {
    Module module = getModule(context);
    if (module != null) {
      // Retrieve the top module
      while (module.getParent() != null) {
        module = module.getParent();
      }
    }
    return Collections.singleton(module);
  }
}
