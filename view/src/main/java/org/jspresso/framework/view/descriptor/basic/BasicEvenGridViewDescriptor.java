/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import java.util.List;

import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * Default implementation of an even grid view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEvenGridViewDescriptor extends BasicCompositeViewDescriptor
    implements IEvenGridViewDescriptor {

  private List<IViewDescriptor> childViewDescriptors;
  private int                   drivingDimension = ROW;
  private int                   drivingDimensionCellCount;

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    return childViewDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public int getDrivingDimension() {
    return drivingDimension;
  }

  /**
   * {@inheritDoc}
   */
  public int getDrivingDimensionCellCount() {
    return drivingDimensionCellCount;
  }

  /**
   * Sets the viewDescriptors.
   * 
   * @param viewDescriptors
   *            the viewDescriptors to set.
   */
  public void setChildViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    this.childViewDescriptors = viewDescriptors;
  }

  /**
   * Sets the drivingDimension.
   * 
   * @param drivingDimension
   *            the drivingDimension to set.
   */
  public void setDrivingDimension(int drivingDimension) {
    this.drivingDimension = drivingDimension;
  }

  /**
   * Sets the drivingDimensionCellCount.
   * 
   * @param drivingDimensionCellCount
   *            the drivingDimensionCellCount to set.
   */
  public void setDrivingDimensionCellCount(int drivingDimensionCellCount) {
    this.drivingDimensionCellCount = drivingDimensionCellCount;
  }

}
