/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.model.IModelProvider;
import com.d2s.framework.util.gate.IGate;

/**
 * A model based gate.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelGate extends IGate {

  /**
   * Sets the modelProvider driving this gate.
   * 
   * @param modelProvider
   *          the modelProvider driving this gate.
   */
  void setModelProvider(IModelProvider modelProvider);

  /**
   * Gets the modelProvider driving this gate.
   * 
   * @return the modelProvider driving this gate.
   */
  IModelProvider getModelProvider();
}
