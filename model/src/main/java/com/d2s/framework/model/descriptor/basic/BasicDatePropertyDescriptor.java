/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Date;

import com.d2s.framework.model.descriptor.IDatePropertyDescriptor;

/**
 * Default implementation of a date descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicDatePropertyDescriptor extends BasicScalarPropertyDescriptor
    implements IDatePropertyDescriptor {

  private String type;

  /**
   * Constructs a new <code>BasicDatePropertyDescriptor</code> instance.
   */
  public BasicDatePropertyDescriptor() {
    type = DATE_TYPE;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return Date.class;
  }

  /**
   * {@inheritDoc}
   */
  public String getType() {
    if (type != null) {
      return type;
    }
    if (getParentDescriptor() != null) {
      return ((IDatePropertyDescriptor) getParentDescriptor()).getType();
    }
    return type;
  }

  /**
   * Sets the type.
   * 
   * @param type
   *            the type to set.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicDatePropertyDescriptor clone() {
    BasicDatePropertyDescriptor clonedDescriptor = (BasicDatePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
