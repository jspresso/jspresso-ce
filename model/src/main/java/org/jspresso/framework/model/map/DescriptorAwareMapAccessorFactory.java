/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.map;

import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * A map accessor factory that create descriptor aware accessors. They are able
 * to handle the underlying model integrity.
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
      Class<?> beanClass, @SuppressWarnings("unused")
      Class<?> elementClass) {
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
      Class<?> beanClass) {
    return new DescriptorAwareMapPropertyAccessor(property);
  }

}
