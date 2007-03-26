/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by objects which might be interested by an
 * underlying model descriptor..
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelDescriptorAware {

  /**
   * Sets the underlying model descriptor.
   * 
   * @param modelDescriptor
   *          the underlying model descriptor.
   */
  void setModelDescriptor(IModelDescriptor modelDescriptor);
}
