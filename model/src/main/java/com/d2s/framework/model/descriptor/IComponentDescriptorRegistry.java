/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;


/**
 * A registry mapping the component contracts with their descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponentDescriptorRegistry {

  /**
   * Retrieves an component descriptor from its contract.
   * 
   * @param componentContract
   *          the component contract.
   * @return th component descriptor.
   */
  IComponentDescriptor getComponentDescriptor(Class componentContract);
}
