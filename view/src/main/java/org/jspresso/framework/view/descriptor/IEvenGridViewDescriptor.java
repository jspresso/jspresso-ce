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
package org.jspresso.framework.view.descriptor;

/**
 * A grid view descriptor which organises its contained views in fixed-sized
 * cells. This kind of described container view might typically be implemented
 * by a swing JPanel with a GridLayout.
 *
 * @author Vincent Vandenschrick
 */
public interface IEvenGridViewDescriptor extends IGridViewDescriptor {

  /**
   * Gets the dimension identifier driving the distribution of the contained
   * components. If {@code LINE}, the grid has an unlimited number of lines
   * but lines have a maximum number of cells. If {@code COLUMN}, the grid
   * has an unlimited number of columns but columns have a maximum number of
   * cells.
   *
   * @return the driving dimension constant.
   */
  EAxis getDrivingDimension();

  /**
   * Gets the number of cells contained in a line of the driving dimension. This
   * is actually a maximum value since a sub view descriptor might span multiple
   * cells.
   *
   * @return the number of sub view descriptors displayed in a row of this view
   *         or 0 if unlimited means that rows are driving the distribution.
   */
  int getDrivingDimensionCellCount();
}
