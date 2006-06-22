/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * This connector is a simple java bean property connector. "Simple" means not a
 * bean reference and not a collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public class BeanSimplePropertyConnector extends BeanPropertyConnector {

  /**
   * Constructs a new bean property connector on a simple bean property.
   */
  BeanSimplePropertyConnector(String property, IAccessorFactory accessorFactory) {
    super(property, accessorFactory);
  }
}
