/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Date;

import com.d2s.framework.model.descriptor.ITimePropertyDescriptor;

/**
 * Default implementation of a time descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTimePropertyDescriptor extends BasicScalarPropertyDescriptor
    implements ITimePropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  public Class getModelType() {
    return Date.class;
  }
}
