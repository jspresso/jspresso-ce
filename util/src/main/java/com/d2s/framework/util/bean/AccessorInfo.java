/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.lang.reflect.Method;

/**
 * This class wraps a method object and helps analyzing it as an accessor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AccessorInfo {

  /**
   * This type of method is an adder ("addToXXX").
   */
  public static final int    ADDER          = 3;

  /**
   * "addTo" prefix.
   */
  public static final String ADDER_PREFIX   = "addTo";

  /**
   * This type of method is a getter (either "getXXX" or "isXXX").
   */
  public static final int    GETTER         = 1;

  /**
   * "get" prefix.
   */
  public static final String GETTER_PREFIX  = "get";

  /**
   * "is" prefix.
   */
  public static final String IS_PREFIX      = "is";

  /**
   * This type of method is not an accessor.
   */
  public static final int    NONE           = 0;

  /**
   * This type of method is a remover ("removeFromXXX").
   */
  public static final int    REMOVER        = 4;

  /**
   * "removeFrom" prefix.
   */
  public static final String REMOVER_PREFIX = "removeFrom";

  /**
   * This type of method is a setter ("setXXX").
   */
  public static final int    SETTER         = 2;

  /**
   * "set" prefix.
   */
  public static final String SETTER_PREFIX  = "set";

  private String             accessedPropertyName;
  private int                accessorType;

  /**
   * Constructs a new <code>AccessorInfo</code> instance. If the method passed
   * in parameter is not an accessor, the <code>getAccessorType</code> method
   * will return <code>NONE</code>.
   * 
   * @param method
   *            the method supposed to be an accessor.
   */
  public AccessorInfo(Method method) {
    String methodName = method.getName();
    Class<?>[] methodArguments = method.getParameterTypes();
    if (methodArguments.length == 0) {
      if (methodName.startsWith(GETTER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, GETTER_PREFIX);
        accessorType = GETTER;
      } else if (methodName.startsWith(IS_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, IS_PREFIX);
        accessorType = GETTER;
      }
    } else if (methodArguments.length == 1) {
      if (methodName.startsWith(SETTER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, SETTER_PREFIX);
        accessorType = SETTER;
      } else if (methodName.startsWith(ADDER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, ADDER_PREFIX);
        accessorType = ADDER;
      } else if (methodName.startsWith(REMOVER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, REMOVER_PREFIX);
        accessorType = REMOVER;
      }
    } else if (methodArguments.length == 2) {
      if (methodName.startsWith(ADDER_PREFIX)) {
        accessedPropertyName = computePropertyName(methodName, ADDER_PREFIX);
        accessorType = ADDER;
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
  public int getAccessorType() {
    return accessorType;
  }

  /**
   * Returns wether the underlying accessor is a modifier (setter, adder or
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
