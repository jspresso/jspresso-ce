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

/**
 * This public interface is implemented by view descriptors which are
 * scrollable.
 *
 * @author Vincent Vandenschrick
 */
public interface IScrollableViewDescriptor extends IViewDescriptor {

  /**
   * Configures the view to be either scaled or scrollable when the display area
   * is too small to display it.
   *
   * @return true if scrollable.
   */
  boolean isScrollable();

  /**
   * Configures the view to be either vertically scaled or scrollable when the
   * display area is too small to display it.
   *
   * @return true if vertically scrollable.
   */
  boolean isVerticallyScrollable();

  /**
   * Configures the view to be either horizontally scaled or scrollable when the
   * display area is too small to display it.
   *
   * @return true if horizontally scrollable.
   */
  boolean isHorizontallyScrollable();
}
