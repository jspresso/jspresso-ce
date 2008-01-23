/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IIntegerPropertyDescriptor;

/**
 * Default implementation of an integer descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicIntegerPropertyDescriptor extends
    BasicNumberPropertyDescriptor implements IIntegerPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return Integer.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicIntegerPropertyDescriptor clone() {
    BasicIntegerPropertyDescriptor clonedDescriptor = (BasicIntegerPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
