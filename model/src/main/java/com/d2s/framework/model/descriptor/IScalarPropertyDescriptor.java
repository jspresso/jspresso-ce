/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is the super-interface of all scalar properties descriptors.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @see com.d2s.framework.model.descriptor.IComponentDescriptor
 */
public interface IScalarPropertyDescriptor extends IPropertyDescriptor {

  /**
   * Gets the default initial value of this scalar property.
   * 
   * @return the default initial value of this scalar property.
   */
  Object getDefaultValue();
}
