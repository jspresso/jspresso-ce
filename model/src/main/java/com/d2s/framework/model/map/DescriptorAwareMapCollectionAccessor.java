/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.map;

import java.util.ArrayList;
import java.util.Collection;

import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptorAware;
import com.d2s.framework.util.accessor.ICollectionAccessor;

/**
 * A map collection property accessor that receives a model descriptor to handle
 * the model integrity.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DescriptorAwareMapCollectionAccessor extends
    DescriptorAwareMapPropertyAccessor implements ICollectionAccessor,
    IModelDescriptorAware {

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
  @SuppressWarnings("unchecked")
  public void addToValue(Object target, Object value) {
    Collection mapValue = getValue(target);
    if (mapValue == null) {
      mapValue = new ArrayList<Object>();
    }
    if (getModelDescriptor() != null) {
      getModelDescriptor().preprocessAdder(this, mapValue, value);
    }
    mapValue.add(value);
    // to trigger a propertyChange.
    setValue(target, mapValue);
    if (getModelDescriptor() != null) {
      getModelDescriptor().postprocessAdder(this, mapValue, value);
    }
  }

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  @Override
  protected ICollectionPropertyDescriptor getModelDescriptor() {
    return (ICollectionPropertyDescriptor) super.getModelDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection getValue(Object target) {
    return (Collection) super.getValue(target);
  }

  /**
   * {@inheritDoc}
   */
  public void removeFromValue(Object target, Object value) {
    Collection mapValue = getValue(target);
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
}
