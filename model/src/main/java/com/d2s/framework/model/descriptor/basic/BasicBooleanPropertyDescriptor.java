/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IBooleanPropertyDescriptor;

/**
 * Default implementation of a boolean descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicBooleanPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IBooleanPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  public Class getModelType() {
    return Boolean.TYPE;
  }

}
