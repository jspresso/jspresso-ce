/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IPasswordPropertyDescriptor;

/**
 * Default implementation of a password descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicPasswordPropertyDescriptor extends
    BasicStringPropertyDescriptor implements IPasswordPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class getModelType() {
    return char[].class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getValueAsString(Object value) {
    if (value != null) {
      return new String((char[]) value);
    }
    return null;
  }
}
