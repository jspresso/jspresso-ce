/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import com.d2s.framework.util.descriptor.IDescriptor;

/**
 * This is just a marker interface for model descriptors (ususally bean
 * descriptors and sub descriptors).
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelDescriptor extends IDescriptor {

  /**
   * Gets the type of the model.
   * 
   * @return the type of the model.
   */
  Class<?> getModelType();
}
