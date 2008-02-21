/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Date;

import com.d2s.framework.model.descriptor.ITimePropertyDescriptor;

/**
 * Default implementation of a time descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  @Override
  public BasicTimePropertyDescriptor clone() {
    BasicTimePropertyDescriptor clonedDescriptor = (BasicTimePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return Date.class;
  }
}
