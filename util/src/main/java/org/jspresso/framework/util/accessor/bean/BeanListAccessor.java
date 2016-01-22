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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.MethodUtils;
import org.jspresso.framework.util.accessor.IListAccessor;
import org.jspresso.framework.util.bean.AccessorInfo;

/**
 * This class is the default implementation of list property accessors.
 *
 * @author Vincent Vandenschrick
 */
public class BeanListAccessor extends BeanCollectionAccessor implements
    IListAccessor {

  private Method adderAtMethod;

  /**
   * Constructs a new default java bean list property accessor.
   *
   * @param property
   *          the property to be accessed.
   * @param beanClass
   *          the java bean class.
   * @param elementClass
   *          the collection element class.
   */
  public BeanListAccessor(String property, Class<?> beanClass,
      Class<?> elementClass) {
    super(property, beanClass, elementClass);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addToValue(Object target, int index, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (adderAtMethod == null) {
      adderAtMethod = MethodUtils.getMatchingAccessibleMethod(getBeanClass(),
          AccessorInfo.ADDER_PREFIX + capitalizeFirst(getProperty()),
          new Class<?>[] {
              Integer.TYPE, getElementClass()
          });
    }
    try {
      adderAtMethod.invoke(getLastNestedTarget(target, getProperty()),
          index, value);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw ex;
    } catch (IllegalArgumentException | NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    }
  }
}
