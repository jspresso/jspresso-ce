/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.accessor.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.bean.PropertyHelper;


/**
 * This class is the default implementation of property accessors. It relies on
 * Jakarta commmons beanutils.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanPropertyAccessor implements IAccessor {

  private Class<?> beanClass;
  private String   property;
  private boolean  writable = true;

  /**
   * Constructs a property accessor based on reflection.
   * 
   * @param property
   *            the property accessed.
   * @param beanClass
   *            the class of the beans accessed using this accessor.
   */
  public BeanPropertyAccessor(String property, Class<?> beanClass) {
    this.property = property;
    this.beanClass = beanClass;

    PropertyDescriptor propertyDescriptor = PropertyHelper
        .getPropertyDescriptor(beanClass, property);
    if (propertyDescriptor != null) {
      this.writable = propertyDescriptor.getWriteMethod() != null;
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    if (target != null) {
      return PropertyUtils.getProperty(target, property);
    }
    return null;
  }

  /**
   * Gets the writable.
   * 
   * @return the writable.
   */
  public boolean isWritable() {
    return writable;
  }

  /**
   * {@inheritDoc}
   */
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (target != null) {
      try {
        PropertyUtils.setProperty(target, property, value);
      } catch (InvocationTargetException ex) {
        if (ex.getTargetException() instanceof RuntimeException) {
          throw (RuntimeException) ex.getTargetException();
        }
        throw ex;
      }
    }
  }

  /**
   * Gets the beanClass property.
   * 
   * @return the beanClass.
   */
  protected Class<?> getBeanClass() {
    return beanClass;
  }

  /**
   * Gets the property property.
   * 
   * @return the property.
   */
  protected String getProperty() {
    return property;
  }
}
