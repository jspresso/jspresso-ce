/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.accessor.map;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * This class is the default implementation of collection property accessors.
 *
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
  @Override
  @SuppressWarnings("unchecked")
  public void addToValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Collection<?> mapValue = getValue(target);
    if (mapValue == null) {
      mapValue = new ArrayList<Object>();
    }
    ((Collection<Object>) mapValue).add(value);
    // to trigger a propertyChange.
    setValue(target, mapValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Collection<?> mapValue = getValue(target);
    if (mapValue != null) {
      mapValue.remove(value);
      // to trigger a propertyChange.
      setValue(target, mapValue);
    }
  }

}
