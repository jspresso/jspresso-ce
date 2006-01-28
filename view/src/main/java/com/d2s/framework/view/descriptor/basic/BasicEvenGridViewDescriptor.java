/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.IEvenGridViewDescriptor;

/**
 * Default implementation of an even grid view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEvenGridViewDescriptor extends BasicGridViewDescriptor
    implements IEvenGridViewDescriptor {

  private int drivingDimensionCellCount;
  private int drivingDimension = ROW;

  /**
   * {@inheritDoc}
   */
  public int getDrivingDimensionCellCount() {
    return drivingDimensionCellCount;
  }

  /**
   * {@inheritDoc}
   */
  public int getDrivingDimension() {
    return drivingDimension;
  }

  /**
   * Sets the drivingDimension.
   * 
   * @param drivingDimension
   *          the drivingDimension to set.
   */
  public void setDrivingDimension(int drivingDimension) {
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
