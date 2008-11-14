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

import java.util.List;

import org.jspresso.framework.view.descriptor.ViewConstraints;

/**
 * A constraint distribution grid container.
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
public class RConstrainedGridContainer extends RContainer {

  private List<ViewConstraints> cellConstraints;
  private List<RComponent>      cells;

  /**
   * Constructs a new <code>RGridContainer</code> instance.
   * 
   * @param guid
   *          the guid
   */
  public RConstrainedGridContainer(String guid) {
    super(guid);
  }

  
  /**
   * Gets the cellConstraints.
   * 
   * @return the cellConstraints.
   */
  public List<ViewConstraints> getCellConstraints() {
    return cellConstraints;
  }

  
  /**
   * Sets the cellConstraints.
   * 
   * @param cellConstraints the cellConstraints to set.
   */
  public void setCellConstraints(List<ViewConstraints> cellConstraints) {
    this.cellConstraints = cellConstraints;
  }

  
  /**
   * Gets the cells.
   * 
   * @return the cells.
   */
  public List<RComponent> getCells() {
    return cells;
  }

  
  /**
   * Sets the cells.
   * 
   * @param cells the cells to set.
   */
  public void setCells(List<RComponent> cells) {
    this.cells = cells;
  }
}
