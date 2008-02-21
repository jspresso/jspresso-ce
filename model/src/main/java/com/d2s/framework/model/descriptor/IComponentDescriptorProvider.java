/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * Marks classes capable of providing a component descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete component type.
 */
public interface IComponentDescriptorProvider<E> extends IModelDescriptor {

  /**
   * Gets the referenced component descriptor.
   * 
   * @return the referenced component descriptor.
   */
  IComponentDescriptor<E> getComponentDescriptor();
}
