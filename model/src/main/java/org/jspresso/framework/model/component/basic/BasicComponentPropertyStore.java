/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.component.basic;

import java.lang.reflect.InvocationTargetException;

import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.component.IComponentPropertyStore;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * Default implementation of {@link IComponentPropertyStore}.
 *
 * @author Vincent Vandenschrick
 */
public class BasicComponentPropertyStore implements IComponentPropertyStore {

  private IAccessorFactory accessorFactory;

  /**
   * Sets accessor factory.
   *
   * @param accessorFactory
   *     the accessor factory
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * {@inheritDoc}
   *
   * @param propertyName
   *     the property name
   * @return the object
   */
  @Override
  public Object get(String propertyName) {
    try {
      return accessorFactory.createPropertyAccessor(propertyName, getClass()).getValue(this);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
      throw new ComponentException(ex,
          "Can not read component property " + propertyName + " on " + getClass().getName());
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param propertyName
   *     the property name
   * @param propertyValue
   *     the property value
   */
  @Override
  public void set(String propertyName, Object propertyValue) {
    try {
      accessorFactory.createPropertyAccessor(propertyName, getClass()).setValue(this, propertyValue);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalArgumentException | IllegalAccessException ex) {
      throw new ComponentException(ex,
          "Can not write component property " + propertyName + " on " + getClass().getName());
    }
  }
}
