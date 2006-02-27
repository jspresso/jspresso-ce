/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * This class wraps a method object and helps analyzing it as an accessor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AccessorInfo {

  /**
   * "get" prefix.
   */
  public static final String GETTER_PREFIX  = "get";

  /**
   * "set" prefix.
   */
  public static final String SETTER_PREFIX  = "set";

  /**
   * "is" prefix.
   */
  public static final String IS_PREFIX      = "is";

  /**
   * "addTo" prefix.
   */
  public static final String ADDER_PREFIX   = "addTo";

  /**
   * "removeFrom" prefix.
   */
  public static final String REMOVER_PREFIX = "removeFrom";

  /**
   * This type of method is not an accessor.
   */
  public static final int    NONE           = 0;

  /**
   * This type of method is a getter (either "getXXX" or "isXXX").
   */
  public static final int    GETTER         = 1;

  /**
   * This type of method is a setter ("setXXX").
   */
  public static final int    SETTER         = 2;

  /**
   * This type of method is an adder ("addToXXX").
   */
  public static final int    ADDER          = 3;

  /**
   * This type of method is a remover ("removeFromXXX").
   */
  public static final int    REMOVER        = 4;

  private int                accessorType;
  private String             accessedPropertyName;

  /**
   * Constructs a new <code>AccessorInfo</code> instance. If the method passed
   * in parameter is not an accessor, the <code>getAccessorType</code> method
   * will return <code>NONE</code>.
   * 
   * @param method
   *          the method supposed to be an accessor.
   */
  public AccessorInfo(Method method) {
    String methodName = method.getName();
    if (methodName.startsWith(GETTER_PREFIX)) {
      accessedPropertyName = computePropertyName(methodName, GETTER_PREFIX);
      accessorType = GETTER;
    } else if (methodName.startsWith(IS_PREFIX)) {
      accessedPropertyName = computePropertyName(methodName, IS_PREFIX);
      accessorType = GETTER;
    } else if (methodName.startsWith(SETTER_PREFIX)) {
      accessedPropertyName = computePropertyName(methodName, SETTER_PREFIX);
      accessorType = SETTER;
    } else if (methodName.startsWith(ADDER_PREFIX)) {
      accessedPropertyName = computePropertyName(methodName, ADDER_PREFIX);
      accessorType = ADDER;
    } else if (methodName.startsWith(REMOVER_PREFIX)) {
      accessedPropertyName = computePropertyName(methodName, REMOVER_PREFIX);
      accessorType = REMOVER;
    }
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
   * Gets the accessedPropertyName.
   * 
   * @return the accessedPropertyName.
   */
  public String getAccessedPropertyName() {
    return accessedPropertyName;
  }

  private static String computePropertyName(String accessorName,
      String accessorPrefix) {
    char firstLetter = Character.toLowerCase(accessorName.charAt(accessorPrefix
        .length()));
    return firstLetter + accessorName.substring(accessorPrefix.length() + 1);
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

  /**
   * Retrieves the collection element class based on the ElementClass
   * annotation.
   * 
   * @param beanClass
   *          the bean class.
   * @param property
   *          the collection property.
   * @return the collection element class.
   */
  public static Class<?> getCollectionElementClass(Class beanClass,
      String property) {
    PropertyDescriptor propertyDescriptor = null;
    try {
      propertyDescriptor = PropertyHelper.getPropertyDescriptor(beanClass,
          property);
    } catch (MissingPropertyException ignored) {
      // ignore until we traverse all interfaces.
    }
    if (propertyDescriptor != null) {
      ElementClass ecAnn = propertyDescriptor.getReadMethod().getAnnotation(
          ElementClass.class);
      if (ecAnn != null) {
        return ecAnn.value();
      }
    }
    // if we reach this point, we may be on a proxy so we might try its
    // implemented interfaces.
    for (Class implementedInterface : beanClass.getInterfaces()) {
      Class<?> collectionElementClass = getCollectionElementClass(
          implementedInterface, property);
      if (collectionElementClass != null) {
        return collectionElementClass;
      }
    }
    return null;
  }
}
