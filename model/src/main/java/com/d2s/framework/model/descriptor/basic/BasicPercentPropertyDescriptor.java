/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IPercentPropertyDescriptor;

/**
 * Default implementation of a percent descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicPercentPropertyDescriptor extends
    BasicDecimalPropertyDescriptor implements IPercentPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPercentPropertyDescriptor clone() {
    BasicPercentPropertyDescriptor clonedDescriptor = (BasicPercentPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
