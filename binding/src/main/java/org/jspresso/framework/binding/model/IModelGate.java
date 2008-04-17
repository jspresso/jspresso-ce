/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.model;

import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.util.gate.IGate;


/**
 * A model based gate.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelGate extends IGate {

  /**
   * Gets the modelProvider driving this gate.
   * 
   * @return the modelProvider driving this gate.
   */
  IModelProvider getModelProvider();

  /**
   * Sets the modelProvider driving this gate.
   * 
   * @param modelProvider
   *            the modelProvider driving this gate.
   */
  void setModelProvider(IModelProvider modelProvider);
}
