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
package org.jspresso.framework.application.backend.action;

import java.util.List;
import java.util.Map;

/**
 * This class can be used as parent class for actions that can be registered
 * either on collection or singleton models.
 *
 * @deprecated since {@code getSelectedModels} does exactly what the class
 *             was meant for and is available to all actions.
 * @author Vincent Vandenschrick
 */
@Deprecated
public abstract class AbstractPolymorphicAction extends BackendAction {

  /**
   * Gets the selected objects from the backend connector and the context. If
   * the action is backed by a collection connector the method will use the
   * context selected indices to compute the selected objects collection.
   * Otherwise, it will simply return a singleton list holding the value of the
   * backend connector.
   *
   * @param context
   *          the action context.
   * @return the list of selected objects.
   * @deprecated use getSelectedModels(Map<String, Object>) instead.
   */
  @Deprecated
  protected List<?> getSelectedObjects(Map<String, Object> context) {
    return getSelectedModels(context);
  }
}
