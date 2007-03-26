/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.Collection;

import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.descriptor.IIconDescriptor;
import com.d2s.framework.util.gate.IGate;

/**
 * The sub views contract.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISubViewDescriptor extends IIconDescriptor {

  /**
   * Gets the model descriptor this view descriptor acts on.
   * 
   * @return the view model descriptor.
   */
  IModelDescriptor getModelDescriptor();

  /**
   * Gets wether this view is read-only.
   * 
   * @return true if the view is read-only.
   */
  boolean isReadOnly();

  /**
   * Gets the collection of gates determining the readability state of this
   * property.
   * 
   * @return the collection of gates determining the readability state of this
   *         property.
   */
  Collection<IGate> getReadabilityGates();

  /**
   * Gets the collection of gates determining the writability state of this
   * property.
   * 
   * @return the collection of gates determining the writability state of this
   *         property.
   */
  Collection<IGate> getWritabilityGates();
}
