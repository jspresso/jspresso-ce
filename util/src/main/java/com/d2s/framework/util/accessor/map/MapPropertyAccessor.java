/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.accessor.map;

import java.util.Map;

import com.d2s.framework.util.accessor.IAccessor;

/**
 * This class is the default implementation of property accessors. It relies on
 * Jakarta commmons beanutils.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
   *          the property accessed.
   */
  public MapPropertyAccessor(String property) {
    this.property = property;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void setValue(Object target, Object value) {
    if (target != null && target instanceof Map) {
      ((Map) target).put(property, value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object getValue(Object target) {
    if (target != null && target instanceof Map) {
      ((Map) target).get(property);
    }
    return null;
  }

  /**
   * Gets the property property.
   * 
   * @return the property.
   */
  protected String getProperty() {
    return property;
  }

  /**
   * Gets the writable.
   * 
   * @return the writable.
   */
  public boolean isWritable() {
    return true;
  }
}
