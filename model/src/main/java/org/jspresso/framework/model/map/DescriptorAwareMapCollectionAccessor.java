/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.map;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * A map collection property accessor that receives a model descriptor to handle
 * the model integrity.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DescriptorAwareMapCollectionAccessor extends
    DescriptorAwareMapPropertyAccessor implements ICollectionAccessor {

  /**
   * Constructs a new <code>DescriptorAwareMapCollectionAccessor</code>
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
    Collection<?> mapValue = getValue(target);
    if (mapValue == null) {
      mapValue = new ArrayList<Object>();
    }
    if (getModelDescriptor() != null) {
      getModelDescriptor().preprocessAdder(this, mapValue, value);
    }
    ((Collection<Object>) mapValue).add(value);
    // to trigger a propertyChange.
    setValue(target, mapValue);
    if (getModelDescriptor() != null) {
      getModelDescriptor().postprocessAdder(this, mapValue, value);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<?> getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    return (Collection<?>) super.getValue(target);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Collection<?> mapValue = getValue(target);
    if (getModelDescriptor() != null) {
      getModelDescriptor().preprocessRemover(this, mapValue, value);
    }
    if (mapValue != null) {
      mapValue.remove(value);
      // to trigger a propertyChange.
      setValue(target, mapValue);
    }
    if (getModelDescriptor() != null) {
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
