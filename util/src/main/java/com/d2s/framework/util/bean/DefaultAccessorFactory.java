/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

/**
 * This is the default implementation of the accessor factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultAccessorFactory implements IAccessorFactory {

  /**
   * Creates a new <code>DefaultPropertyAccessor</code> on the property.
   * <p>
   * {@inheritDoc}
   */
  public IAccessor createPropertyAccessor(String property, Class beanClass) {
    return new DefaultPropertyAccessor(property, beanClass);
  }

  /**
   * Creates a new <code>DefaultCollectionAccessor</code> on the collection
   * property.
   * <p>
   * {@inheritDoc}
   */
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class beanClass, Class elementClass) {
    return new DefaultCollectionAccessor(property, beanClass, elementClass);
  }
}
