/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.util.gui.CellConstraints;
import org.jspresso.framework.view.descriptor.IConstrainedGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This composite view arranges its children in a grid where cell behaviour and
 * dimensions are configured using cell constraints. A cell constraint is a
 * simple data structure holding the following properties :
 * <ul>
 * <li><code>row</code>: the row to which the cell belongs</li>
 * <li><code>column</code>: the column to which the cell belongs</li>
 * <li><code>width</code>: the number of columns the cell spans horizontally
 * (default value is 1)</li>
 * <li><code>height</code>: the number of rows the cell spans vertically
 * (default value is 1)</li>
 * <li><code>heightResizable</code>: wether the cell should be resized to take
 * all the available space vertically</li>
 * <li><code>widthResizable</code>: wether the cell should be resized to take
 * all the available space horizontally</li>
 * </ul>
 * Default cascading order follows the order of nested view registrations in the
 * container.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicConstrainedGridViewDescriptor extends
    BasicCompositeViewDescriptor implements IConstrainedGridViewDescriptor {

  private Map<IViewDescriptor, CellConstraints> cells;

  /**
   * {@inheritDoc}
   */
  public CellConstraints getCellConstraints(IViewDescriptor viewDescriptor) {
    if (cells != null) {
      return cells.get(viewDescriptor);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IViewDescriptor> childViewDescriptors = new ArrayList<IViewDescriptor>();
    if (cells != null) {
      boolean leading = true;
      for (Map.Entry<IViewDescriptor, CellConstraints> constrainedCell : cells
          .entrySet()) {
        completeChildDescriptor(constrainedCell.getKey(), leading);
        leading = false;
        childViewDescriptors.add(constrainedCell.getKey());
      }
    }
    return childViewDescriptors;
  }

  /**
   * Deprecated. Use <code>cells</code> instead.
   * 
   * @param constrainedViews
   *          the constrainedViews to set.
   * @deprecated use setCells instead.
   */
  @Deprecated
  public void setConstrainedViews(
      Map<IViewDescriptor, CellConstraints> constrainedViews) {
    setCells(constrainedViews);
  }

  /**
   * Deprecated. Use <code>cells</code> instead.
   * 
   * @param constrainedCells
   *          the constrainedCells to set.
   * @deprecated use setCells instead.
   */
  @Deprecated
  public void setConstrainedCells(
      Map<IViewDescriptor, CellConstraints> constrainedCells) {
    setCells(constrainedCells);
  }

  /**
   * Registers the nested children views along with their cell constaints. They
   * are set as a <code>Map</code> that is :
   * <ul>
   * <li>keyed by the children views</li>
   * <li>valued by the cell constraints to apply to each nested view</li>
   * </ul>
   * 
   * @param cells
   *          the cells to set.
   */
  public void setCells(Map<IViewDescriptor, CellConstraints> cells) {
    this.cells = cells;
  }
}
