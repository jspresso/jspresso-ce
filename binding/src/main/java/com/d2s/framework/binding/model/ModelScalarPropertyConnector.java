/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.model.descriptor.IScalarPropertyDescriptor;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * This connector is a simple java model property connector. "Simple" means not a
 * model reference and not a collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public class ModelScalarPropertyConnector extends ModelPropertyConnector {

  /**
   * Constructs a new model property connector on a simple model property.
   */
  ModelScalarPropertyConnector(IScalarPropertyDescriptor modelDescriptor, IAccessorFactory accessorFactory) {
    super(modelDescriptor, accessorFactory);
  }
}
