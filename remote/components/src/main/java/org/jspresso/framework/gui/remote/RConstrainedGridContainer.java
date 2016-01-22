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

import org.jspresso.framework.util.gui.CellConstraints;

/**
 * A constraint distribution grid container.
 *
 * @author Vincent Vandenschrick
 */
public class RConstrainedGridContainer extends RContainer {

  private static final long serialVersionUID = 6330184608837643072L;

  private CellConstraints[] cellConstraints;
  private RComponent[]      cells;

  /**
   * Constructs a new {@code RGridContainer} instance.
   *
   * @param guid
   *          the guid
   */
  public RConstrainedGridContainer(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RConstrainedGridContainer} instance. Only used
   * for GWT serialization support.
   */
  public RConstrainedGridContainer() {
    // For serialization support
  }

  /**
   * Gets the cellConstraints.
   *
   * @return the cellConstraints.
   */
  public CellConstraints[] getCellConstraints() {
    return cellConstraints;
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
   * Sets the cellConstraints.
   *
   * @param cellConstraints
   *          the cellConstraints to set.
   */
  public void setCellConstraints(CellConstraints... cellConstraints) {
    this.cellConstraints = cellConstraints;
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
}
