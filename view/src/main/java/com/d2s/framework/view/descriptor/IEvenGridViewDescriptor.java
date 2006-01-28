/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * A grid view descriptor which organises its contained views in fixed-sized
 * cells. This kind of described container view might typically be implemented
 * by a swing JPanel with a GridLayout.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEvenGridViewDescriptor extends IGridViewDescriptor {

  /**
   * <code>ROW</code> dimension constant.
   */
  int ROW    = 1;

  /**
   * <code>COLUMN</code> dimension constant.
   */
  int COLUMN = 2;

  /**
   * Gets the number of cells contained in a line of the driving dimension. This
   * is actually a maximum value since a sub view descriptor might span multiple
   * cells.
   * 
   * @return the number of sub view descriptors displayed in a row of this view
   *         or 0 if unlimited means that rows are driving the distribution.
   */
  int getDrivingDimensionCellCount();

  /**
   * Gets the dimension identifier driving the distribution of the contained
   * components. If <code>LINE</code>, the grid has an unlimited number of
   * lines but lines have a maximum number of cells. If <code>COLUMN</code>,
   * the grid has an unlimited number of columns but columns have a maximum
   * number of cells.
   * 
   * @return the driving dimension constant.
   */
  int getDrivingDimension();
}
