/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.io.Serializable;

import com.d2s.framework.model.descriptor.IJavaSerializablePropertyDescriptor;

/**
 * Default implementation of a java serialized property descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public BasicJavaSerializablePropertyDescriptor clone() {
    BasicJavaSerializablePropertyDescriptor clonedDescriptor = (BasicJavaSerializablePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return Serializable.class;
  }
}
