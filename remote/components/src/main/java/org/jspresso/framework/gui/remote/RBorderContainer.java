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
 * A border (north, south, east, west, center) container.
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
public class RBorderContainer extends RContainer {
  
  private RComponent north;
  private RComponent south;
  private RComponent east;
  private RComponent west;
  private RComponent center;

  /**
   * Constructs a new <code>RBorderContainer</code> instance.
   * 
   * @param guid the guid
   */
  public RBorderContainer(String guid) {
    super(guid);
  }

  
  /**
   * Gets the north.
   * 
   * @return the north.
   */
  public RComponent getNorth() {
    return north;
  }

  
  /**
   * Sets the north.
   * 
   * @param north the north to set.
   */
  public void setNorth(RComponent north) {
    this.north = north;
  }

  
  /**
   * Gets the south.
   * 
   * @return the south.
   */
  public RComponent getSouth() {
    return south;
  }

  
  /**
   * Sets the south.
   * 
   * @param south the south to set.
   */
  public void setSouth(RComponent south) {
    this.south = south;
  }

  
  /**
   * Gets the east.
   * 
   * @return the east.
   */
  public RComponent getEast() {
    return east;
  }

  
  /**
   * Sets the east.
   * 
   * @param east the east to set.
   */
  public void setEast(RComponent east) {
    this.east = east;
  }

  
  /**
   * Gets the west.
   * 
   * @return the west.
   */
  public RComponent getWest() {
    return west;
  }

  
  /**
   * Sets the west.
   * 
   * @param west the west to set.
   */
  public void setWest(RComponent west) {
    this.west = west;
  }

  
  /**
   * Gets the center.
   * 
   * @return the center.
   */
  public RComponent getCenter() {
    return center;
  }

  
  /**
   * Sets the center.
   * 
   * @param center the center to set.
   */
  public void setCenter(RComponent center) {
    this.center = center;
  }
}
