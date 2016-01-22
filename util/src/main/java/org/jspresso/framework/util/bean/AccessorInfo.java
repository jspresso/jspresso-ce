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
package org.jspresso.framework.util.bean;

import java.lang.reflect.Method;

/**
 * This class wraps a method object and helps analyzing it as an accessor.
 *
 * @author Vincent Vandenschrick
 */
public class AccessorInfo {

  /**
   * "addTo" prefix.
   */
  public static final String ADDER_PREFIX   = "addTo";

  /**
   * "get" prefix.
   */
  public static final String GETTER_PREFIX  = "get";

  /**
   * "is" prefix.
   */
  public static final String IS_PREFIX      = "is";

  /**
   * "removeFrom" prefix.
   */
  public static final String REMOVER_PREFIX = "removeFrom";

  /**
   * "set" prefix.
   */
  public static final String SETTER_PREFIX  = "set";

  private String             accessedPropertyName;
  private EAccessorType      accessorType;

  /**
   * Constructs a new {@code AccessorInfo} instance. If the method passed
   * in parameter is not an accessor, the {@code getAccessorType} method
   * will return {@code NONE}.
   *
   * @param method
   *          the method supposed to be an accessor.
   */
  public AccessorInfo(Method method) {
    accessorType = EAccessorType.NONE;
    String methodName = method.getName();
    Class<?>[] methodArguments = method.getParameterTypes();
    if (methodArguments.length == 0) {
      if (methodName.startsWith(GETTER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, GETTER_PREFIX);
        accessorType = EAccessorType.GETTER;
      } else if (methodName.startsWith(IS_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, IS_PREFIX);
        accessorType = EAccessorType.GETTER;
      }
    } else if (methodArguments.length == 1) {
      if (methodName.startsWith(SETTER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, SETTER_PREFIX);
        accessorType = EAccessorType.SETTER;
      } else if (methodName.startsWith(ADDER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, ADDER_PREFIX);
        accessorType = EAccessorType.ADDER;
      } else if (methodName.startsWith(REMOVER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, REMOVER_PREFIX);
        accessorType = EAccessorType.REMOVER;
      }
    } else if (methodArguments.length == 2) {
      if (methodName.startsWith(ADDER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, ADDER_PREFIX);
        accessorType = EAccessorType.ADDER;
      }
    }
  }

  private static String computePropertyName(String accessorName,
      String accessorPrefix) {
    char firstLetter = Character.toLowerCase(accessorName.charAt(accessorPrefix
        .length()));
    return firstLetter + accessorName.substring(accessorPrefix.length() + 1);
  }

  /**
   * Gets the accessedPropertyName.
   *
   * @return the accessedPropertyName.
   */
  public String getAccessedPropertyName() {
    return accessedPropertyName;
  }

  /**
   * Gets the accessorType.
   *
   * @return the accessorType.
   */
  public EAccessorType getAccessorType() {
    return accessorType;
  }

  /**
   * Returns whether the underlying accessor is a modifier (setter, adder or
   * remover).
   *
   * @return true if the underlying accessor is a modifier.
   */
  public boolean isModifier() {
    switch (accessorType) {
      case SETTER:
      case ADDER:
      case REMOVER:
        return true;
      default:
    }
    return false;
  }
}
