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
 * An evenly distributed grid container.
 *
 * @author Vincent Vandenschrick
 */
public class REvenGridContainer extends RContainer {

  private static final long serialVersionUID = -7545449789544595947L;

  private RComponent[]      cells;
  private String            drivingDimension;
  private int               drivingDimensionCellCount;

  /**
   * Constructs a new {@code REvenGridContainer} instance.
   *
   * @param guid
   *          the guid
   */
  public REvenGridContainer(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code REvenGridContainer} instance. Only used for
   * GWT serialization support.
   */
  public REvenGridContainer() {
    // For serialization support
  }

  /**
   * Gets the cells.
   *
   * @return the cells.
   */
  public RComponent[] getCells() {
    return cells;
  }

  /**
   * Gets the drivingDimension.
   *
   * @return the drivingDimension.
   */
  public String getDrivingDimension() {
    return drivingDimension;
  }

  /**
   * Gets the drivingDimensionCellCount.
   *
   * @return the drivingDimensionCellCount.
   */
  public int getDrivingDimensionCellCount() {
    return drivingDimensionCellCount;
  }

  /**
   * Sets the cells.
   *
   * @param cells
   *          the cells to set.
   */
  public void setCells(RComponent... cells) {
    this.cells = cells;
  }

  /**
   * Sets the drivingDimension.
   *
   * @param drivingDimension
   *          the drivingDimension to set.
   */
  public void setDrivingDimension(String drivingDimension) {
    this.drivingDimension = drivingDimension;
  }

  /**
   * Sets the drivingDimensionCellCount.
   *
   * @param drivingDimensionCellCount
   *          the drivingDimensionCellCount to set.
   */
  public void setDrivingDimensionCellCount(int drivingDimensionCellCount) {
    this.drivingDimensionCellCount = drivingDimensionCellCount;
  }
}
