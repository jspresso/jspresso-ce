/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.model;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;


/**
 * The model connector contract.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelValueConnector extends IValueConnector {

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  IModelDescriptor getModelDescriptor();

  /**
   * Gets the modelProvider.
   * 
   * @return the modelProvider.
   */
  IModelProvider getModelProvider();

  /**
   * Sets the modelDescriptor.
   * 
   * @param modelDescriptor
   *            the modelDescriptor.
   */
  void setModelDescriptor(IModelDescriptor modelDescriptor);

}
