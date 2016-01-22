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
import java.util.Map;

import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.accessor.map.MapPropertyAccessor;

/**
 * A map property accessor that receives a model descriptor to handle the model
 * integrity.
 *
 * @author Vincent Vandenschrick
 */
public class DescriptorAwareMapPropertyAccessor extends MapPropertyAccessor
    implements IModelDescriptorAware {

  private IModelDescriptor modelDescriptor;

  /**
   * Constructs a new {@code DescriptorAwareMapPropertyAccessor} instance.
   *
   * @param property
   *          the property to create the accessor for.
   */
  public DescriptorAwareMapPropertyAccessor(String property) {
    super(property);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Object oldValue = getValue(target);
    Object actualNewValue = value;
    // target instance must be tested to avoid triggering twice the property
    // processors if the map contains a non-map model.
    if (target instanceof Map<?, ?> && getModelDescriptor() != null) {
      actualNewValue = getModelDescriptor().interceptSetter(target, value);
      getModelDescriptor().preprocessSetter(target, actualNewValue);
    }
    super.setValue(target, actualNewValue);
    // target instance must be tested to avoid triggering twice the property
    // processors if the map contains a non-map model.
    if (target instanceof Map<?, ?> && getModelDescriptor() != null) {
      getModelDescriptor().postprocessSetter(target, oldValue, actualNewValue);
    }
  }

  /**
   * Gets the modelDescriptor.
   *
   * @return the modelDescriptor.
   */
  protected IPropertyDescriptor getModelDescriptor() {
    return (IPropertyDescriptor) modelDescriptor;
  }
}
