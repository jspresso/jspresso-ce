/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.accessor.map;

import java.util.ArrayList;
import java.util.Collection;

import com.d2s.framework.util.accessor.ICollectionAccessor;

/**
 * This class is the default implementation of collection property accessors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MapCollectionAccessor extends MapPropertyAccessor implements
    ICollectionAccessor {

  /**
   * Constructs a new default java bean collection property accessor.
   *
   * @param property
   *          the property to be accessed.
   */
  public MapCollectionAccessor(String property) {
    super(property);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void addToValue(Object target, Object value) {
    Collection mapValue = getValue(target);
    if (mapValue == null) {
      mapValue = new ArrayList<Object>();
    }
    mapValue.add(value);
    // to trigger a propertyChange.
    setValue(target, mapValue);
  }

  /**
   * {@inheritDoc}
   */
  public void removeFromValue(Object target, Object value) {
    Collection mapValue = getValue(target);
    if (mapValue != null) {
      mapValue.remove(value);
      // to trigger a propertyChange.
      setValue(target, mapValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection getValue(Object target) {
    return (Collection) super.getValue(target);
  }

}
