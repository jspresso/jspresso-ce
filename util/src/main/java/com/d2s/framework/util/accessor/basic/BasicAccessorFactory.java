/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.accessor.basic;

import java.util.Map;

import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;

/**
 * This is the default implementation of the accessor factory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicAccessorFactory implements IAccessorFactory {

  private IAccessorFactory beanAccessorFactory;
  private IAccessorFactory mapAccessorFactory;

  /**
   * {@inheritDoc}
   */
  public ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class<?> beanClass, Class<?> elementClass) {
    return getAccessorDelegate(beanClass).createCollectionPropertyAccessor(
        property, beanClass, elementClass);
  }

  private IAccessorFactory getAccessorDelegate(Class<?> beanClass) {
    IAccessorFactory delegate;
    if (beanClass == null || Map.class.isAssignableFrom(beanClass)) {
      delegate = mapAccessorFactory;
    } else {
      delegate = beanAccessorFactory;
    }
    return delegate;
  }

  /**
   * {@inheritDoc}
   */
  public IAccessor createPropertyAccessor(String property, Class<?> beanClass) {
    return getAccessorDelegate(beanClass).createPropertyAccessor(property,
        beanClass);
  }

  
  /**
   * Sets the beanAccessorFactory.
   * 
   * @param beanAccessorFactory the beanAccessorFactory to set.
   */
  public void setBeanAccessorFactory(IAccessorFactory beanAccessorFactory) {
    this.beanAccessorFactory = beanAccessorFactory;
  }

  
  /**
   * Sets the mapAccessorFactory.
   * 
   * @param mapAccessorFactory the mapAccessorFactory to set.
   */
  public void setMapAccessorFactory(IAccessorFactory mapAccessorFactory) {
    this.mapAccessorFactory = mapAccessorFactory;
  }

}
