/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.INumberPropertyDescriptor;

/**
 * Default implementation of a number descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicNumberPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements INumberPropertyDescriptor {

  private Double minValue;
  private Double maxValue;

  /**
   * {@inheritDoc}
   */
  public Double getMinValue() {
    if (minValue != null) {
      return minValue;
    }
    if (getParentDescriptor() != null) {
      return ((INumberPropertyDescriptor) getParentDescriptor()).getMinValue();
    }
    return minValue;
  }

  /**
   * {@inheritDoc}
   */
  public Double getMaxValue() {
    if (maxValue != null) {
      return maxValue;
    }
    if (getParentDescriptor() != null) {
      return ((INumberPropertyDescriptor) getParentDescriptor()).getMaxValue();
    }
    return maxValue;
  }

  /**
   * Sets the maxValue property.
   * 
   * @param maxValue
   *          the maxValue to set.
   */
  public void setMaxValue(Double maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minValue property.
   * 
   * @param minValue
   *          the minValue to set.
   */
  public void setMinValue(Double minValue) {
    this.minValue = minValue;
  }
}
