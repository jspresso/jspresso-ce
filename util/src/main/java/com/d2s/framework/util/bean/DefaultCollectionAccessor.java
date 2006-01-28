/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.MethodUtils;

/**
 * This class is the default implementation of collection property accessors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultCollectionAccessor extends DefaultPropertyAccessor
    implements ICollectionAccessor {

  private Class  elementClass;
  private Method adderMethod;
  private Method removerMethod;

  /**
   * Constructs a new default java bean collection property accessor.
   * 
   * @param property
   *          the property to be accessed.
   * @param beanClass
   *          the java bean class.
   * @param elementClass
   *          the type of the collection elements.
   */
  public DefaultCollectionAccessor(String property, Class beanClass,
      Class elementClass) {
    super(property, beanClass);
    this.elementClass = elementClass;
  }

  /**
   * {@inheritDoc}
   */
  public void addToValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (adderMethod == null) {
      adderMethod = MethodUtils.getMatchingAccessibleMethod(getBeanClass(),
          AccessorInfo.ADDER_PREFIX + capitalizeFirst(getProperty()),
          new Class[] {elementClass});
    }
    adderMethod.invoke(target, new Object[] {value});
  }

  /**
   * {@inheritDoc}
   */
  public void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (removerMethod == null) {
      removerMethod = MethodUtils.getMatchingAccessibleMethod(getBeanClass(),
          AccessorInfo.REMOVER_PREFIX + capitalizeFirst(getProperty()),
          new Class[] {elementClass});
    }
    removerMethod.invoke(target, new Object[] {value});
  }

  private String capitalizeFirst(String input) {
    return Character.toUpperCase(input.charAt(0)) + input.substring(1);
  }

}
