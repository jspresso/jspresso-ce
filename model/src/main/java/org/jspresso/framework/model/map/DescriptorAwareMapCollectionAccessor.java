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
package org.jspresso.framework.model.map;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * A map collection property accessor that receives a model descriptor to handle
 * the model integrity.
 *
 * @author Vincent Vandenschrick
 */
public class DescriptorAwareMapCollectionAccessor extends
    DescriptorAwareMapPropertyAccessor implements ICollectionAccessor {

  /**
   * Constructs a new {@code DescriptorAwareMapCollectionAccessor}
   * instance.
   *
   * @param property
   *          the property to create the accessor for.
   */
  public DescriptorAwareMapCollectionAccessor(String property) {
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
    Collection<Object> mapValue = getValue(target);
    if (mapValue == null) {
      mapValue = new ArrayList<>();
    }
    // target instance must be tested to avoid triggering twice the property
    // processors if the map contains a non-map model.
    if (target instanceof Map<?, ?> && getModelDescriptor() != null) {
      getModelDescriptor().preprocessAdder(this, mapValue, value);
    }
    mapValue.add(value);
    // to trigger a propertyChange.
    setValue(target, mapValue);
    // target instance must be tested to avoid triggering twice the property
    // processors if the map contains a non-map model.
    if (target instanceof Map<?, ?> && getModelDescriptor() != null) {
      getModelDescriptor().postprocessAdder(this, mapValue, value);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Collection<Object> mapValue = getValue(target);
    // target instance must be tested to avoid triggering twice the property
    // processors if the map contains a non-map model.
    if (target instanceof Map<?, ?> && getModelDescriptor() != null) {
      getModelDescriptor().preprocessRemover(this, mapValue, value);
    }
    if (mapValue != null) {
      mapValue.remove(value);
      // to trigger a propertyChange.
      setValue(target, mapValue);
    }
    // target instance must be tested to avoid triggering twice the property
    // processors if the map contains a non-map model.
    if (target instanceof Map<?, ?> && getModelDescriptor() != null) {
      getModelDescriptor().postprocessRemover(this, mapValue, value);
    }
  }

  /**
   * Gets the modelDescriptor.
   *
   * @return the modelDescriptor.
   */
  @Override
  protected ICollectionPropertyDescriptor<?> getModelDescriptor() {
    return (ICollectionPropertyDescriptor<?>) super.getModelDescriptor();
  }
}
