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
package org.jspresso.framework.util.accessor.bean;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.jspresso.framework.util.accessor.AbstractPropertyAccessor;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.bean.PropertyHelper;

/**
 * This class is the default implementation of bean property accessors. It
 * relies on Jakarta commons beanutils.
 *
 * @author Vincent Vandenschrick
 */
public class BeanPropertyAccessor extends AbstractPropertyAccessor {

  private final Class<?> beanClass;
  private boolean  writable;

  /**
   * Constructs a property accessor based on reflection.
   *
   * @param property
   *          the property accessed.
   * @param beanClass
   *          the class of the beans accessed using this accessor.
   */
  public BeanPropertyAccessor(String property, Class<?> beanClass) {
    super(property);
    this.beanClass = computeTargetBeanClass(beanClass, getProperty());
    String lastNestedProperty = getLastNestedProperty();
    if (Map.class.isAssignableFrom(getBeanClass()) && !PropertyHelper.getPropertyNames(getBeanClass()).contains(
        lastNestedProperty)) {
      // we deal with map values
      this.writable = true;
    } else {
      PropertyDescriptor propertyDescriptor = PropertyHelper.getPropertyDescriptor(getBeanClass(), lastNestedProperty);
      if (propertyDescriptor != null) {
        this.writable = propertyDescriptor.getWriteMethod() != null;
      }
    }
  }

  /**
   * Gets the writable.
   *
   * @return the writable.
   */
  @Override
  public boolean isWritable() {
    return writable;
  }

  /**
   * Gets the beanClass property.
   *
   * @return the beanClass.
   */
  protected Class<?> getBeanClass() {
    return beanClass;
  }

  private Class<?> computeTargetBeanClass(Class<?> beanClazz, String prop) {
    int indexOfNestedDelim = prop.indexOf(IAccessor.NESTED_DELIM);
    if (indexOfNestedDelim < 0) {
      return beanClazz;
    }
    Class<?> rootClass = PropertyHelper.getPropertyType(beanClazz,
        prop.substring(0, indexOfNestedDelim));
    return computeTargetBeanClass(rootClass,
        prop.substring(indexOfNestedDelim + 1));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean appliesTo(Object target) {
    return target == null || !(target instanceof Map<?, ?>);
  }
}
