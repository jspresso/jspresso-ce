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
package org.jspresso.framework.view.action;

/**
 * An actionable object.
 *
 * @author Vincent Vandenschrick
 */
public interface IActionable {

  /**
   * Retrieves a map of action lists to be presented on this view. Actions
   * should be grouped based on their kind (for instance a list of edit actions,
   * a list of business actions...) and will be presented accordingly. For
   * instance, each action list will be presented in a different menu in a menu
   * bar, will be separated from the others by a separator in a toolbar, ...
   *
   * @return the map of action lists handled by this view.
   */
  ActionMap getActionMap();

  /**
   * Retrieves a secondary map of action lists to be presented on this view.
   * Actions in this map should be visually distinguished from the main action
   * map, e.g. placed in another toolbar.
   *
   * @return the secondary map of action lists handled by this view.
   */
  ActionMap getSecondaryActionMap();
}
