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
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.util.gui.ERenderingOptions;
import org.jspresso.framework.view.action.ActionList;

/**
 * An action view descriptor.
 *
 * @author Vincent Vandenschrick
 */
public interface IActionViewDescriptor extends IViewDescriptor {

  /**
   * Gets the action that is presented to the user through this view.
   *
   * @return the action that is presented to the user through this view.
   */
  IAction getAction();

  /**
   * Gets the action list that is presented to the user through this view.
   *
   * @return the action list that is presented to the user through this view.
   */
  ActionList getActionList();

  /**
   * Gets the renderingOptions.
   *
   * @return the renderingOptions.
   */
  ERenderingOptions getRenderingOptions();
}
