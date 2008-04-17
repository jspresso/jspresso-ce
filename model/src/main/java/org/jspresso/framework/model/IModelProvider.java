/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model;

import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;

/**
 * This public interface should be implemented by any class being able to
 * provide a model instance. It allows for registration of
 * <code>IModelChangeListener</code> which can keep track of model object
 * changes.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelProvider {

  /**
   * Adds a new bean listener to this model provider.
   * 
   * @param listener
   *            The added listener
   */
  void addModelChangeListener(IModelChangeListener listener);

  /**
   * Gets the bean object of this provider.
   * 
   * @return The bean object
   */
  Object getModel();

  /**
   * Gets the bean object of this provider.
   * 
   * @return The bean object
   */
  IComponentDescriptorProvider<?> getModelDescriptor();

  /**
   * Removes a bean listener from this model provider.
   * 
   * @param listener
   *            The added listener
   */
  void removeModelChangeListener(IModelChangeListener listener);
}
