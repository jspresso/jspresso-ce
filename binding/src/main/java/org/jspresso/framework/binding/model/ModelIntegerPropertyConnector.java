/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.model;

import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;


/**
 * This connector is a simple integer model property connector. It handles
 * setting java.lang.Long and java.lang.Integer values based on the connector
 * value and the property type.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
