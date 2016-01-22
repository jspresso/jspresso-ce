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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.view.descriptor.IHtmlViewDescriptor;

/**
 * This type of view descriptor is used to display a a string property
 * containing HTML text. The objective is to be able to configure scrollability
 * of the HTML component.
 *
 * @author Vincent Vandenschrick
 */
public class BasicHtmlViewDescriptor extends BasicPropertyViewDescriptor
    implements IHtmlViewDescriptor {

  private boolean verticallyScrollable;
  private boolean horizontallyScrollable;

  /**
   * Constructs a new {@code BasicImageViewDescriptor} instance.
   */
  protected BasicHtmlViewDescriptor() {
    verticallyScrollable = true;
    horizontallyScrollable = false;
  }

  /**
   * Gets the scrollable.
   *
   * @return the scrollable.
   */
  @Override
  public boolean isScrollable() {
    return isVerticallyScrollable() || isHorizontallyScrollable();
  }

  /**
   * Gets the verticallyScrollable.
   *
   * @return the verticallyScrollable.
   */
  @Override
  public boolean isVerticallyScrollable() {
    return verticallyScrollable;
  }

  /**
   * Configures the view to be either vertically cropped or scrollable when the
   * display area is too small to display it.
   *
   * @param verticallyScrollable
   *          the verticallyScrollable to set.
   */
  public void setVerticallyScrollable(boolean verticallyScrollable) {
    this.verticallyScrollable = verticallyScrollable;
  }

  /**
   * Configures the view to be either horizontally cropped or scrollable when the
   * display area is too small to display it.
   *
   * @return the horizontallyScrollable.
   */
  @Override
  public boolean isHorizontallyScrollable() {
    return horizontallyScrollable;
  }

  /**
   * Sets the horizontallyScrollable.
   *
   * @param horizontallyScrollable
   *          the horizontallyScrollable to set.
   */
  public void setHorizontallyScrollable(boolean horizontallyScrollable) {
    this.horizontallyScrollable = horizontallyScrollable;
  }

}
