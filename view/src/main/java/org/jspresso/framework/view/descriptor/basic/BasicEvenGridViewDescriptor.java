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

import java.util.List;

import org.jspresso.framework.view.descriptor.EAxis;
import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This composite view arranges its children in a grid where cells are
 * distributed evenly. All cells are resized horizontally and vertically to fill
 * its available space.
 * <p>
 * The number of cells in a row / column is determined by the combination of the
 * {@code drivingDimension} and {@code drivingDimensionCellCount}
 * properties. the cells are spread along the driving dimension (row or column)
 * until the maximum number of cells in the dimension has been reached. Then a
 * new row (or column) is added. The process repeats until all the cells have
 * been added.
 * <p>
 * This container does not allow for individual cell configuration like
 * row/column spanning. Whenever cell disposition has to be customized more
 * finely, a {@code BasicConstrainedGridViewDescriptor} should be used
 * instead.
 * <p>
 * Default cascading order follows the order of nested view registrations in the
 * container.
 *
 * @author Vincent Vandenschrick
 */
public class BasicEvenGridViewDescriptor extends BasicCompositeViewDescriptor
    implements IEvenGridViewDescriptor {

  private List<IViewDescriptor> cells;
  private EAxis                 drivingDimension          = EAxis.ROW;
  private int                   drivingDimensionCellCount = 2;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    if (cells != null) {
      IViewDescriptor previousViewDescriptor = null;
      for (IViewDescriptor cell : cells) {
        completeChildDescriptor(cell, previousViewDescriptor);
        previousViewDescriptor = cell;
      }
    }
    return cells;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EAxis getDrivingDimension() {
    return drivingDimension;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getDrivingDimensionCellCount() {
    return drivingDimensionCellCount;
  }

  /**
   * Registers the nested views to display as grid cells.
   *
   * @param cells
   *          the cells to set.
   */
  public void setCells(List<IViewDescriptor> cells) {
    this.cells = cells;
  }

  /**
   * Sets the viewDescriptors.
   *
   * @param viewDescriptors
   *          the viewDescriptors to set.
   * @deprecated use setCells instead.
   */
  @Deprecated
  public void setChildViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    setCells(viewDescriptors);
  }

  /**
   * Configures the driving dimension of the grid. This is either a value of the
   * {@code EAxis} enum or its equivalent string representation :
   * <ul>
   * <li>{@code ROW} for distributing cells along rows then columns</li>
   * <li>{@code COLUMN} for distributing cells along columns then rows</li>
   * </ul>
   * Default value is {@code EAxis.ROW}, i.e. distribute cells along rows
   * then columns.
   *
   * @param drivingDimension
   *          the drivingDimension to set.
   */
  public void setDrivingDimension(EAxis drivingDimension) {
    this.drivingDimension = drivingDimension;
  }

  /**
   * This property configures the maximum number of cells in the driving
   * dimension (row or column). Nested views are distributed along the driving
   * axis until this maximum number has been reached. A new row or column is
   * then created to host the remaining cells.
   *
   * @param drivingDimensionCellCount
   *          the drivingDimensionCellCount to set.
   */
  public void setDrivingDimensionCellCount(int drivingDimensionCellCount) {
    this.drivingDimensionCellCount = drivingDimensionCellCount;
  }

}
