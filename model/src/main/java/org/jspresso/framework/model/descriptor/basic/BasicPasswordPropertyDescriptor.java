/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import org.jspresso.framework.model.descriptor.IPasswordPropertyDescriptor;

/**
 * Default implementation of a password descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicPasswordPropertyDescriptor extends
    BasicStringPropertyDescriptor implements IPasswordPropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicPasswordPropertyDescriptor clone() {
    BasicPasswordPropertyDescriptor clonedDescriptor = (BasicPasswordPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return char[].class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getValueAsString(Object value) {
    if (value != null) {
      return new String((char[]) value);
    }
    return null;
  }
}
