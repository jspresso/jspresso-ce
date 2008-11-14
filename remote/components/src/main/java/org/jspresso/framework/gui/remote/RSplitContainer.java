/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * A splitted container.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision: 1486 $
 * @author Vincent Vandenschrick
 */
public class RSplitContainer extends RContainer {

  private RComponent leftTop;
  private RComponent rightBottom;
  private String     orientation;

  /**
   * Constructs a new <code>RSplitContainer</code> instance.
   * 
   * @param guid
   *          the guid
   */
  public RSplitContainer(String guid) {
    super(guid);
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
   * Gets the orientation.
   * 
   * @return the orientation.
   */
  public String getOrientation() {
    return orientation;
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
   * Sets the leftTop.
   * 
   * @param leftTop the leftTop to set.
   */
  public void setLeftTop(RComponent leftTop) {
    this.leftTop = leftTop;
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
   * Sets the rightBottom.
   * 
   * @param rightBottom the rightBottom to set.
   */
  public void setRightBottom(RComponent rightBottom) {
    this.rightBottom = rightBottom;
  }
}
