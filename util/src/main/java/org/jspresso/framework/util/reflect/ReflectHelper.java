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
package org.jspresso.framework.util.reflect;

import java.lang.reflect.Field;

/**
 * Helper class for reflection.
 *
 * @author Vincent Vandenschrick
 */
public final class ReflectHelper {

  /**
   * Constructs a new {@code ReflectHelper} instance.
   */
  private ReflectHelper() {
    // Helper constructor.
  }

  /**
   * Gets a private field value.
   *
   * @param clazz
   *          the class declaring the private field.
   * @param fieldName
   *          the field name.
   * @param target
   *          the target object
   * @return the private field value or null if not found.
   * @throws IllegalAccessException
   *           if accessibility context prevents from accessing the field.
   * @throws NoSuchFieldException
   *           if the field does not exist.
   */
  public static Object getPrivateFieldValue(Class<?> clazz, String fieldName,
      Object target) throws IllegalAccessException, NoSuchFieldException {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  /**
   * Sets a private field value.
   *
   * @param clazz
   *          the class declaring the private field.
   * @param fieldName
   *          the field name.
   * @param target
   *          the target object
   * @param fieldValue
   *          the private field value or null if not found.
   * @throws IllegalAccessException
   *           if accessibility context prevents from accessing the field.
   * @throws NoSuchFieldException
   *           if the field does not exist.
   */
  public static void setPrivateFieldValue(Class<?> clazz, String fieldName,
      Object target, Object fieldValue) throws IllegalAccessException,
      NoSuchFieldException {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, fieldValue);
  }
}
