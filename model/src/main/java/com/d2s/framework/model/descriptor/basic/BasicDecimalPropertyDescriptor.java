/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IDecimalPropertyDescriptor;

/**
 * Default implementation of a decimal descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  @Override
  public BasicDecimalPropertyDescriptor clone() {
    BasicDecimalPropertyDescriptor clonedDescriptor = (BasicDecimalPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

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
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return Double.class;
  }

  /**
   * Sets the maxFractionDigit property.
   * 
   * @param maxFractionDigit
   *            the maxFractionDigit to set.
   */
  public void setMaxFractionDigit(Integer maxFractionDigit) {
    this.maxFractionDigit = maxFractionDigit;
  }
}
