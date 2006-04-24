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

  private Long maxMillis;

  /**
   * {@inheritDoc}
   */
  public long getMaxMillis() {
    if (maxMillis != null) {
      return maxMillis.longValue();
    }
    if (getParentDescriptor() != null) {
      return ((IDurationPropertyDescriptor) getParentDescriptor())
          .getMaxMillis();
    }
    return 0;
  }

  /**
   * Sets the maxMillis property.
   * 
   * @param maxMillis
   *          the maxMillis to set.
   */
  public void setMaxMillis(long maxMillis) {
    this.maxMillis = new Long(maxMillis);
  }

  /**
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return Long.class;
  }
}
