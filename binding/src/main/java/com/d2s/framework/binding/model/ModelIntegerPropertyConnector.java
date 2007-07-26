/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.model.descriptor.IIntegerPropertyDescriptor;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * This connector is a simple integer model property connector. It handles
 * setting java.lang.Long and java.lang.Integer values based on the connector
 * value and the property type.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public class ModelIntegerPropertyConnector extends ModelScalarPropertyConnector {

  /**
   * Constructs a new model property connector on a simple model property.
   */
  ModelIntegerPropertyConnector(IIntegerPropertyDescriptor modelDescriptor,
      IAccessorFactory accessorFactory) {
    super(modelDescriptor, accessorFactory);
  }

  /**
   * Accesses the underlying model property and sets its value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue instanceof Long) {
      super.setConnecteeValue(new Integer(((Long) aValue).intValue()));
    } else {
      super.setConnecteeValue(aValue);
    }
  }
}
