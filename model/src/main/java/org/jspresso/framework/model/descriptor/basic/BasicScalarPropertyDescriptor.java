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
package org.jspresso.framework.model.descriptor.basic;

import java.lang.reflect.InvocationTargetException;

import org.jspresso.framework.model.descriptor.DescriptorException;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;

/**
 * This is the root abstract descriptor for all property descriptors that are
 * not relationship end properties. This includes, for instance, strings,
 * numbers, dates, binary content, and so on.
 *
 * @author Vincent Vandenschrick
 */
public abstract class BasicScalarPropertyDescriptor extends
    BasicPropertyDescriptor implements IScalarPropertyDescriptor {

  private Object defaultValue;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicScalarPropertyDescriptor clone() {
    BasicScalarPropertyDescriptor clonedDescriptor = (BasicScalarPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * Gets the defaultValue.
   *
   * @return the defaultValue.
   */
  @Override
  public Object getDefaultValue() {
    return defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return getDelegateClassName() == null;
  }

  /**
   * Sets the property default value. When a component owning this property is
   * instantiated, its properties are initialized using their default values. By
   * default, a property default value is {@code null}.
   * <p>
   * This incoming value can be either the actual property default value (as an
   * {@code Object}) or its string representation whose parsing will be
   * delegated to the property descriptor.
   *
   * @param defaultValue
   *          the defaultValue to set.
   */
  public void setDefaultValue(Object defaultValue) {
    if (defaultValue != null) {
      if (defaultValue.getClass().isAssignableFrom(getModelType())) {
        this.defaultValue = defaultValue;
      } else {
        try {
          this.defaultValue = parseStringValue(defaultValue.toString());
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InstantiationException |
            SecurityException ex) {
          throw new DescriptorException(ex);
        } catch (InvocationTargetException ex) {
          if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          throw new DescriptorException(ex.getCause());
        }
      }
    } else {
      this.defaultValue = null;
    }
  }

  /**
   * Parses a value given as String. Calls the model type constructor using the
   * String parameter.
   *
   * @param valueAsString
   *          the value to set as String.
   * @return the parsed value.
   * @throws InstantiationException
   *           whenever an exception occurs.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   */
  protected Object parseStringValue(String valueAsString)
      throws InstantiationException, IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    return getModelType().getConstructor(String.class).newInstance(
        valueAsString);
  }
}
