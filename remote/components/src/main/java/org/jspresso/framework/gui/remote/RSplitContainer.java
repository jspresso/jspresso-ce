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
package org.jspresso.framework.gui.remote;

/**
 * A split container.
 *
 * @author Vincent Vandenschrick
 */
public class RSplitContainer extends RContainer {

  private static final long serialVersionUID = 4276881870150436168L;

  private RComponent        leftTop;
  private String            orientation;
  private RComponent        rightBottom;

  /**
   * Constructs a new {@code RSplitContainer} instance.
   *
   * @param guid
   *          the guid
   */
  public RSplitContainer(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RSplitContainer} instance. Only used for
   * serialization support.
   */
  public RSplitContainer() {
    // For serialization support
  }

  /**
   * Gets the leftTop.
   *
   * @return the leftTop.
   */
  public RComponent getLeftTop() {
    return leftTop;
  }

  /**
   * Gets the orientation.
   *
   * @return the orientation.
   */
  public String getOrientation() {
    return orientation;
  }

  /**
   * Gets the rightBottom.
   *
   * @return the rightBottom.
   */
  public RComponent getRightBottom() {
    return rightBottom;
  }

  /**
   * Sets the leftTop.
   *
   * @param leftTop
   *          the leftTop to set.
   */
  public void setLeftTop(RComponent leftTop) {
    this.leftTop = leftTop;
  }

  /**
   * Sets the orientation.
   *
   * @param orientation
   *          the orientation to set.
   */
  public void setOrientation(String orientation) {
    this.orientation = orientation;
  }

  /**
   * Sets the rightBottom.
   *
   * @param rightBottom
   *          the rightBottom to set.
   */
  public void setRightBottom(RComponent rightBottom) {
    this.rightBottom = rightBottom;
  }
}
