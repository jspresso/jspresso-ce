/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

import java.util.Collection;

import org.jspresso.framework.model.component.IComponent;


/**
 * A registry mapping the component contracts with their descriptors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   *            the component contract.
   * @return th component descriptor.
   */
  IComponentDescriptor<?> getComponentDescriptor(
      Class<? extends IComponent> componentContract);

  /**
   * Gets all the registered component descriptors.
   * 
   * @return all the registered component descriptors.
   */
  Collection<IComponentDescriptor<?>> getComponentDescriptors();
}
