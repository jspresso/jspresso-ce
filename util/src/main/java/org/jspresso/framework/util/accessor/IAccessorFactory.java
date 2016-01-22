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
package org.jspresso.framework.util.accessor;

import java.lang.reflect.Method;

import org.jspresso.framework.util.bean.AccessorInfo;

/**
 * This interface specify the contract of property accessor factories.
 *
 * @author Vincent Vandenschrick
 */
public interface IAccessorFactory extends IAccessorFactoryProvider {

  /**
   * Creates a new java bean collection property accessor.
   *
   * @param property
   *          the property to be accessed.
   * @param beanClass
   *          the java bean class.
   * @param elementClass
   *          the collection element class.
   * @return the collection property accessor.
   */
  ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class<?> beanClass, Class<?> elementClass);

  /**
   * Creates a new java bean property accessor .
   *
   * @param property
   *          the property to be accessed.
   * @param beanClass
   *          the java bean class.
   * @return the property accessor.
   */
  IAccessor createPropertyAccessor(String property, Class<?> beanClass);

  /**
   * Retrieves accessor information from a method.
   *
   * @param method
   *          the method to retrieve info for.
   * @return the accessor info.
   */
  AccessorInfo getAccessorInfo(Method method);
}
