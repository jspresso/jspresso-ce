/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.accessor.bean;

import java.util.List;

import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;
import com.d2s.framework.util.bean.PropertyHelper;

/**
 * This is the default implementation of the accessor factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanAccessorFactory implements IAccessorFactory {

  /**
   * Creates a new <code>BeanPropertyAccessor</code> on the property.
   * <p>
   * {@inheritDoc}
   */
  public IAccessor createPropertyAccessor(String property, Class beanClass) {
    return new BeanPropertyAccessor(property, beanClass);
  }

  /**
   * Creates a new <code>BeanCollectionAccessor</code> on the collection
   * property.
   * <p>
   * {@inheritDoc}
   */
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class beanClass, Class elementClass) {
    if (List.class.isAssignableFrom(PropertyHelper.getPropertyType(beanClass,
        property))) {
      return new BeanListAccessor(property, beanClass, elementClass);
    }
    return new BeanCollectionAccessor(property, beanClass, elementClass);
  }
}
