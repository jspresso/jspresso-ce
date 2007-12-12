/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.accessor.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.bean.PropertyHelper;

/**
 * This class is the default implementation of property accessors. It relies on
 * Jakarta commmons beanutils.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanPropertyAccessor implements IAccessor {

  private Class<?> beanClass;
  private String   property;
  private boolean  writable = true;

  /**
   * Constructs a property accessor based on reflection.
   * 
   * @param property
   *            the property accessed.
   * @param beanClass
   *            the class of the beans accessed using this accessor.
   */
  public BeanPropertyAccessor(String property, Class<?> beanClass) {
    this.property = property;
    this.beanClass = beanClass;

    try {
      PropertyDescriptor propertyDescriptor = PropertyHelper
          .getPropertyDescriptor(beanClass, property);
      if (propertyDescriptor != null) {
        this.writable = propertyDescriptor.getWriteMethod() != null;
      }
    } catch (RuntimeException e) {
      throw e;
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    if (target != null) {
      return PropertyUtils.getProperty(target, property);
    }
    return null;
  }

  /**
   * Gets the writable.
   * 
   * @return the writable.
   */
  public boolean isWritable() {
    return writable;
  }

  /**
   * {@inheritDoc}
   */
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (target != null) {
      PropertyUtils.setProperty(target, property, value);
    }
  }

  /**
   * Gets the beanClass property.
   * 
   * @return the beanClass.
   */
  protected Class<?> getBeanClass() {
    return beanClass;
  }

  /**
   * Gets the property property.
   * 
   * @return the property.
   */
  protected String getProperty() {
    return property;
  }
}
