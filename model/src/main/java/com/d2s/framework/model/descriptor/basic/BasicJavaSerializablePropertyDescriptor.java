/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.io.Serializable;

import com.d2s.framework.model.descriptor.IJavaSerializablePropertyDescriptor;

/**
 * Default implementation of a java serialized property descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicJavaSerializablePropertyDescriptor extends
    BasicBinaryPropertyDescriptor implements
    IJavaSerializablePropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return Serializable.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicJavaSerializablePropertyDescriptor clone() {
    BasicJavaSerializablePropertyDescriptor clonedDescriptor = (BasicJavaSerializablePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
