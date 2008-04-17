/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.model;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;


/**
 * Interface for all factories of model connectors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelConnectorFactory {

  /**
   * Creates a model connector based on a model descriptor.
   * 
   * @param id
   *            the connector identifier.
   * @param modelDescriptor
   *            the model descriptor to create the connector for.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor);

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
