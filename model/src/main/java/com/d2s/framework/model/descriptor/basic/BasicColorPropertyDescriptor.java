/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IColorPropertyDescriptor;

/**
 * Default implementation of a color descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicColorPropertyDescriptor extends BasicScalarPropertyDescriptor
    implements IColorPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicColorPropertyDescriptor clone() {
    BasicColorPropertyDescriptor clonedDescriptor = (BasicColorPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return String.class;
  }
}
