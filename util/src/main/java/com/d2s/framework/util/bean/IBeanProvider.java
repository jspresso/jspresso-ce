/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

/**
 * This public interface should be implemented by any class being able to
 * provide a bean instance. It allows for registration of
 * <code>IBeanChangeListener</code> which can keep track of bean object
 * changes.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBeanProvider {

  /**
   * Gets the bean object of this provider.
   * 
   * @return The bean object
   */
  IPropertyChangeCapable getBean();

  /**
   * Gets the bean type of this provider.
   * 
   * @return The bean class object
   */
  Class getBeanClass();

  /**
   * Adds a new bean listener to this bean provider.
   * 
   * @param listener
   *          The added listener
   */
  void addBeanChangeListener(IBeanChangeListener listener);

  /**
   * Removes a bean listener from this bean provider.
   * 
   * @param listener
   *          The added listener
   */
  void removeBeanChangeListener(IBeanChangeListener listener);
}
