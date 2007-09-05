/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.accessor.map;

import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;

/**
 * This is the map implementation of the accessor factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MapAccessorFactory implements IAccessorFactory {

  /**
   * Creates a new <code>MapCollectionAccessor</code> on the collection
   * property.
   * <p>
   * {@inheritDoc}
   */
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      @SuppressWarnings("unused")
      Class beanClass, @SuppressWarnings("unused")
      Class elementClass) {
    return new MapCollectionAccessor(property);
  }

  /**
   * Creates a new <code>MapPropertyAccessor</code> on the property.
   * <p>
   * {@inheritDoc}
   */
  public IAccessor createPropertyAccessor(String property,
      @SuppressWarnings("unused")
      Class beanClass) {
    return new MapPropertyAccessor(property);
  }
}
