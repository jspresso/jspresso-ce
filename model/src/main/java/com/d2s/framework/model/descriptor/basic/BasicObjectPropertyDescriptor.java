/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IObjectPropertyDescriptor;

/**
 * Default implementation of an arbitrary object property descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicObjectPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IObjectPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicObjectPropertyDescriptor clone() {
    BasicObjectPropertyDescriptor clonedDescriptor = (BasicObjectPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * Returns Object class.
   * <p>
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
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
