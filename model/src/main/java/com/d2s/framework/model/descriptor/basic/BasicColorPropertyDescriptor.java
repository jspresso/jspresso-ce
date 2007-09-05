/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IColorPropertyDescriptor;

/**
 * Default implementation of a color descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
  public Class<?> getModelType() {
    return String.class;
  }

}
