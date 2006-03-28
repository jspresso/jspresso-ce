/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IBinaryPropertyDescriptor;

/**
 * Default implementation of a binary descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicBinaryPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IBinaryPropertyDescriptor {

  private Integer maxLength;

  /**
   * {@inheritDoc}
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the maxLength property.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return byte[].class;
  }
}
