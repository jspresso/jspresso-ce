/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IDurationPropertyDescriptor;

/**
 * Default implementation of a duration descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicDurationPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IDurationPropertyDescriptor {

  private long maxMillis = 0;

  /**
   * {@inheritDoc}
   */
  public long getMaxMillis() {
    return maxMillis;
  }

  /**
   * Sets the maxMillis property.
   * 
   * @param maxMillis
   *          the maxMillis to set.
   */
  public void setMaxMillis(long maxMillis) {
    this.maxMillis = maxMillis;
  }

  /**
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return Long.class;
  }
}
