/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.map;

import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;

/**
 * A map accessor factory that create descriptor aware accessors. They are able
 * to handle the underlying model integrity.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DescriptorAwareMapAccessorFactory implements IAccessorFactory {

  /**
   * Creates a new <code>DescriptorAwareMapCollectionAccessor</code> on the
   * collection property.
   * <p>
   * {@inheritDoc}
   */
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      @SuppressWarnings("unused")
      Class beanClass, @SuppressWarnings("unused")
      Class elementClass) {
    return new DescriptorAwareMapCollectionAccessor(property);
  }

  /**
   * Creates a new <code>DescriptorAwareMapPropertyAccessor</code> on the
   * property.
   * <p>
   * {@inheritDoc}
   */
  public IAccessor createPropertyAccessor(String property,
      @SuppressWarnings("unused")
      Class beanClass) {
    return new DescriptorAwareMapPropertyAccessor(property);
  }

}
