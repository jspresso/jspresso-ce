/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * Marks classes capable of providing a component descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponentDescriptorProvider {

  /**
   * Gets the referenced component descriptor.
   * 
   * @return the referenced component descriptor.
   */
  IComponentDescriptor getComponentDescriptor();
}
