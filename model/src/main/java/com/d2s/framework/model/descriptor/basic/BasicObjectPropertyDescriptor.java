/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IObjectPropertyDescriptor;

/**
 * Default implementation of an arbitrary object property descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicObjectPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IObjectPropertyDescriptor {

  /**
   * Returns Object class.
   * <p>
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return Object.class;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return false;
  }
}
