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
 * This interface is implemented by any bean value accessor.
 *
 * @author Vincent Vandenschrick
 */
public interface IAccessor {

  /**
   * {@code NESTED_DELIM} is '.' constant for nested properties.
   */
  char NESTED_DELIM = '.';

  /**
   * Gets the value from the target of this accessor.
   *
   * @param <T>
   *     type inference return.
   * @param target
   *          the target from which to get the value.
   * @return the value obtained.
   * @throws NoSuchMethodException
   *           if a matching method is not found.
   * @throws InvocationTargetException
   *           if the underlying method throws an exception.
   * @throws IllegalAccessException
   *           if this {@code Method} object enforces Java language access
   *           control and the underlying method is inaccessible.
   */
  <T> T getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException;

  /**
   * Gets the writability of this accessor .
   *
   * @return true if this accessor has a mutator.
   */
  boolean isWritable();

  /**
   * Sets the value on the target of this accessor.
   *
   * @param target
   *          the target on which to set the value.
   * @param value
   *          the value to set.
   * @throws InvocationTargetException
   *           if the underlying method throws an exception.
   * @throws IllegalAccessException
   *           if this {@code Method} object enforces Java language access
   *           control and the underlying method is inaccessible.
   * @throws NoSuchMethodException
   *           if a matching method is not found.
   */
  void setValue(Object target, Object value) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException;

  /**
   * Tests whether the accessor applies to the parameter target.
   *
   * @param target
   *          the target to test the accessor for.
   * @return {@code true} if the accessor can be used to access the
   *         parameter target.
   */
  boolean appliesTo(Object target);
}
