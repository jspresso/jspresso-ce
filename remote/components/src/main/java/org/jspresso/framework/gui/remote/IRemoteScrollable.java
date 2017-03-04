/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.remote;

/**
 * The interface Remote scrollable.
 */
public interface IRemoteScrollable {

  /**
   * Is vertically scrollable boolean.
   *
   * @return the boolean
   */
  boolean isVerticallyScrollable();

  /**
   * Sets vertically scrollable.
   *
   * @param verticallyScrollable
   *     the vertically scrollable
   */
  void setVerticallyScrollable(boolean verticallyScrollable);

  /**
   * Is horizontally scrollable boolean.
   *
   * @return the boolean
   */
  boolean isHorizontallyScrollable();

  /**
   * Sets horizontally scrollable.
   *
   * @param horizontallyScrollable
   *     the horizontally scrollable
   */
  void setHorizontallyScrollable(boolean horizontallyScrollable);
}
