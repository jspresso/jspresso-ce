/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IBooleanPropertyDescriptor;

/**
 * Default implementation of a boolean descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public Class<?> getModelType() {
    return Boolean.TYPE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicBooleanPropertyDescriptor clone() {
    BasicBooleanPropertyDescriptor clonedDescriptor = (BasicBooleanPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
