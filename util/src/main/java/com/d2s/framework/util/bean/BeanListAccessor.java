/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.beanutils.MethodUtils;

import com.d2s.framework.util.access.IListAccessor;

/**
 * This class is the default implementation of list property accessors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanListAccessor extends BeanCollectionAccessor implements
    IListAccessor {

  private Method adderAtMethod;

  /**
   * Constructs a new default java bean list property accessor.
   * 
   * @param property
   *          the property to be accessed.
   * @param beanClass
   *          the java bean class.
   */
  public BeanListAccessor(String property, Class beanClass) {
    super(property, beanClass);
  }

  /**
   * {@inheritDoc}
   */
  public void addToValue(Object target, int index, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (adderAtMethod == null) {
      adderAtMethod = MethodUtils.getMatchingAccessibleMethod(getBeanClass(),
          AccessorInfo.ADDER_PREFIX + capitalizeFirst(getProperty()),
          new Class[] {
              Integer.TYPE,
              AccessorInfo.getCollectionElementClass(getBeanClass(),
                  getProperty())});
    }
    adderAtMethod.invoke(target, new Object[] {new Integer(index), value});
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    return (List) super.getValue(target);
  }
}
