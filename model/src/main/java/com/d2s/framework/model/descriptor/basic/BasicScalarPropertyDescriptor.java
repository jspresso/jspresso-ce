/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IScalarPropertyDescriptor;

/**
 * Default implementation of a property descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicScalarPropertyDescriptor extends
    BasicPropertyDescriptor implements IScalarPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return true;
  }

}
