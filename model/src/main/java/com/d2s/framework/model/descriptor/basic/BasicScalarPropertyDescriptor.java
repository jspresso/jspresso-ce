/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IScalarPropertyDescriptor;

/**
 * Default implementation of a property descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicScalarPropertyDescriptor extends
    BasicPropertyDescriptor implements IScalarPropertyDescriptor {

  private Object defaultValue;

  /**
   * Gets the defaultValue.
   * 
   * @return the defaultValue.
   */
  public Object getDefaultValue() {
    return defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return true;
  }

  /**
   * Sets the defaultValue.
   * 
   * @param defaultValue
   *            the defaultValue to set.
   */
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicScalarPropertyDescriptor clone() {
    BasicScalarPropertyDescriptor clonedDescriptor = (BasicScalarPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
