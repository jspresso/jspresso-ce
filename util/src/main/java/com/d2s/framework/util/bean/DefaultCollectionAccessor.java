/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

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

  private Method adderMethod;
  private Method removerMethod;

  /**
   * Constructs a new default java bean collection property accessor.
   * 
   * @param property
   *          the property to be accessed.
   * @param beanClass
   *          the java bean class.
   */
  public DefaultCollectionAccessor(String property, Class beanClass) {
    super(property, beanClass);
  }

  /**
   * {@inheritDoc}
   */
  public void addToValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (adderMethod == null) {
      adderMethod = MethodUtils.getMatchingAccessibleMethod(getBeanClass(),
          AccessorInfo.ADDER_PREFIX + capitalizeFirst(getProperty()),
          new Class[] {AccessorInfo.getCollectionElementClass(getBeanClass(),
              getProperty())});
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
          new Class[] {AccessorInfo.getCollectionElementClass(getBeanClass(),
              getProperty())});
    }
    removerMethod.invoke(target, new Object[] {value});
  }

  /**
   * Capitalizes the first caracter of a string.
   * 
   * @param input
   *          the string to capitalize the first caracter.
   * @return the transformed string.
   */
  protected String capitalizeFirst(String input) {
    return Character.toUpperCase(input.charAt(0)) + input.substring(1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    return (Collection) super.getValue(target);
  }

}
