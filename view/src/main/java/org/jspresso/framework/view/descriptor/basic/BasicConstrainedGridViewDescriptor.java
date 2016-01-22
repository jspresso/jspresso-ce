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
 * <li>{@code row}: the row to which the cell belongs</li>
 * <li>{@code column}: the column to which the cell belongs</li>
 * <li>{@code width}: the number of columns the cell spans horizontally
 * (default value is 1)</li>
 * <li>{@code height}: the number of rows the cell spans vertically
 * (default value is 1)</li>
 * <li>{@code heightResizable}: whether the cell should be resized to take
 * all the available space vertically</li>
 * <li>{@code widthResizable}: whether the cell should be resized to take
 * all the available space horizontally</li>
 * </ul>
 * Default cascading order follows the order of nested view registrations in the
 * container.
 *
 * @author Vincent Vandenschrick
 */
public class BasicConstrainedGridViewDescriptor extends
    BasicCompositeViewDescriptor implements IConstrainedGridViewDescriptor {

  private Map<IViewDescriptor, CellConstraints> cells;

  /**
   * {@inheritDoc}
   */
  @Override
  public CellConstraints getCellConstraints(IViewDescriptor viewDescriptor) {
    if (cells != null) {
      return cells.get(viewDescriptor);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IViewDescriptor> childViewDescriptors = new ArrayList<>();
    IViewDescriptor peviousChildViewDescriptor = null;
    if (cells != null) {
      for (Map.Entry<IViewDescriptor, CellConstraints> constrainedCell : cells
          .entrySet()) {
        completeChildDescriptor(constrainedCell.getKey(),
            peviousChildViewDescriptor);
        childViewDescriptors.add(constrainedCell.getKey());
        peviousChildViewDescriptor = constrainedCell.getKey();
      }
    }
    return childViewDescriptors;
  }

  /**
   * Registers the nested children views along with their cell constraints. They
   * are set as a {@code Map} that is :
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

  /**
   * Deprecated. Use {@code cells} instead.
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
   * Deprecated. Use {@code cells} instead.
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
}
