/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IComponentDescriptorRegistry;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * Interface for all factories of model connectors.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelConnectorFactory {

  /**
   * Creates a model connector based on a model descriptor.
   * 
   * @param modelDescriptor
   *            the model descriptor to create the connector for.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(IModelDescriptor modelDescriptor);

  /**
   * Gets the <code>IAccessorFactory</code> used.
   * 
   * @return the used accessor factory
   */
  IAccessorFactory getAccessorFactory();

  /**
   * Gets the <code>IComponentDescriptorRegistry</code> used.
   * 
   * @return the used descriptor registry
   */
  IComponentDescriptorRegistry getDescriptorRegistry();
}
