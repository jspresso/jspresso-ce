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

import java.lang.reflect.InvocationTargetException;

/**
 * This interface is implemented by any bean value accessor on a collection
 * property.
 *
 * @author Vincent Vandenschrick
 */
public interface ICollectionAccessor extends IAccessor {

  /**
   * Adds the value to the bean property of this accessor.
   *
   * @param target
   *          the target on which to add the value.
   * @param value
   *          the value to add.
   * @throws IllegalAccessException
   *           if the underlying method throws an exception.
   * @throws InvocationTargetException
   *           if this {@code Method} object enforces Java language access
   *           control and the underlying method is inaccessible.
   * @throws NoSuchMethodException
   *           if a matching method is not found.
   */
  void addToValue(Object target, Object value) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException;

  /**
   * Removes the value from the bean property of this accessor.
   *
   * @param target
   *          the target on which to remove the value.
   * @param value
   *          the value to remove.
   * @throws IllegalAccessException
   *           if the underlying method throws an exception.
   * @throws InvocationTargetException
   *           if this {@code Method} object enforces Java language access
   *           control and the underlying method is inaccessible.
   * @throws NoSuchMethodException
   *           if a matching method is not found.
   */
  void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException;
}
