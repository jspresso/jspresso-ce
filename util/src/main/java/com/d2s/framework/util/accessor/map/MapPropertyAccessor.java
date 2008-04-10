/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.accessor.map;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.d2s.framework.util.accessor.IAccessor;

/**
 * This class is the default implementation of property accessors. It relies on
 * Jakarta commmons beanutils.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MapPropertyAccessor implements IAccessor {

  private String property;

  /**
   * Constructs a map property accessor.
   * 
   * @param property
   *            the property accessed.
   */
  public MapPropertyAccessor(String property) {
    this.property = property;
  }

  /**
   * {@inheritDoc}
   */
  public Object getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    // if (target != null && target instanceof Map) {
    // return ((Map<?, ?>) target).get(property);
    // }
    // PropertyUtils can also handle maps
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
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (target != null) {
      try {
        PropertyUtils.setProperty(target, property, value);
      } catch (InvocationTargetException ex) {
        if (ex.getTargetException() instanceof RuntimeException) {
          throw (RuntimeException) ex.getTargetException();
        }
        throw ex;
      }
    }
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
