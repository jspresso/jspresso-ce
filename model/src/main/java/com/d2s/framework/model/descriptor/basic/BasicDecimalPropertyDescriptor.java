/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IDecimalPropertyDescriptor;

/**
 * Default implementation of a decimal descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicDecimalPropertyDescriptor extends
    BasicNumberPropertyDescriptor implements IDecimalPropertyDescriptor {

  private Integer maxFractionDigit;

  /**
   * {@inheritDoc}
   */
  public Integer getMaxFractionDigit() {
    if (maxFractionDigit != null) {
      return maxFractionDigit;
    }
    if (getParentDescriptor() != null) {
      return ((IDecimalPropertyDescriptor) getParentDescriptor())
          .getMaxFractionDigit();
    }
    return maxFractionDigit;
  }

  /**
   * Sets the maxFractionDigit property.
   * 
   * @param maxFractionDigit
   *          the maxFractionDigit to set.
   */
  public void setMaxFractionDigit(Integer maxFractionDigit) {
    this.maxFractionDigit = maxFractionDigit;
  }

  /**
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return Double.class;
  }
}
