/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.map;

import org.jspresso.framework.util.accessor.AbstractAccessorFactory;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * A map accessor factory that create descriptor aware accessors. They are able
 * to handle the underlying model integrity.
 * 
 * @author Vincent Vandenschrick
 */
public class DescriptorAwareMapAccessorFactory extends AbstractAccessorFactory {

  /**
   * Creates a new {@code DescriptorAwareMapCollectionAccessor} on the
   * collection property.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class<?> beanClass, Class<?> elementClass) {
    return new DescriptorAwareMapCollectionAccessor(property);
  }

  /**
   * Creates a new {@code DescriptorAwareMapPropertyAccessor} on the
   * property.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IAccessor createPropertyAccessor(String property, Class<?> beanClass) {
    return new DescriptorAwareMapPropertyAccessor(property);
  }

}
