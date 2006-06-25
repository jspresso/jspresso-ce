/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IIntegerPropertyDescriptor;

/**
 * Default implementation of an integer descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
  public Class getModelType() {
    return Integer.class;
  }

}
