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
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.bean.AccessorInfo;

/**
 * This class is the default implementation of collection property accessors.
 *
 * @author Vincent Vandenschrick
 */
public class BeanCollectionAccessor extends BeanPropertyAccessor implements
    ICollectionAccessor {

  private Method   adderMethod;
  private final Class<?> elementClass;
  private Method   removerMethod;

  /**
   * Constructs a new default java bean collection property accessor.
   *
   * @param property
   *          the property to be accessed.
   * @param beanClass
   *          the java bean class.
   * @param elementClass
   *          the collection element class.
   */
  public BeanCollectionAccessor(String property, Class<?> beanClass,
      Class<?> elementClass) {
    super(property, beanClass);
    this.elementClass = elementClass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addToValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (adderMethod == null) {
      adderMethod = MethodUtils.getMatchingAccessibleMethod(getBeanClass(),
          AccessorInfo.ADDER_PREFIX + capitalizeFirst(getProperty()),
          new Class<?>[] {
            getElementClass()
          });
    }
    try {
      adderMethod.invoke(getLastNestedTarget(target, getProperty()),
          value);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw ex;
    } catch (IllegalArgumentException | NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (removerMethod == null) {
      removerMethod = MethodUtils.getMatchingAccessibleMethod(getBeanClass(),
          AccessorInfo.REMOVER_PREFIX + capitalizeFirst(getProperty()),
          new Class<?>[] {
            getElementClass()
          });
    }
    try {
      removerMethod.invoke(getLastNestedTarget(target, getProperty()),
          value);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw ex;
    } catch (IllegalArgumentException | NoSuchMethodException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Capitalizes the first character of a string.
   *
   * @param input
   *          the string to capitalize the first character.
   * @return the transformed string.
   */
  protected String capitalizeFirst(String input) {
    return Character.toUpperCase(input.charAt(0)) + input.substring(1);
  }

  /**
   * Gets the elementClass.
   *
   * @return the elementClass.
   */
  protected Class<?> getElementClass() {
    return elementClass;
  }

}
