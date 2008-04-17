/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.model;

import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;


/**
 * This connector is a simple java model property connector. "Simple" means not
 * a model reference and not a collection.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public class ModelScalarPropertyConnector extends ModelPropertyConnector {

  /**
   * Constructs a new model property connector on a simple model property.
   */
  ModelScalarPropertyConnector(IScalarPropertyDescriptor modelDescriptor,
      IAccessorFactory accessorFactory) {
    super(modelDescriptor, accessorFactory);
  }
}
