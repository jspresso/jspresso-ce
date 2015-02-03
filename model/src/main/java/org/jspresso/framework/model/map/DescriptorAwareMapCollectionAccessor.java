/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
