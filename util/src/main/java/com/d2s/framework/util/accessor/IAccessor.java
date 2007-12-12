/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.accessor;

import java.lang.reflect.InvocationTargetException;

/**
 * This interface is implemented by any bean value accessor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IAccessor {

  /**
   * Gets the value from the target of this accessor.
   * 
   * @param target
   *            the target from which to get the value.
   * @return the value obtained.
   * @throws NoSuchMethodException
   *             if a matching method is not found.
   * @throws InvocationTargetException
   *             if the underlying method throws an exception.
   * @throws IllegalAccessException
   *             if this <code>Method</code> object enforces Java language
   *             access control and the underlying method is inaccessible.
   */
  Object getValue(Object target) throws IllegalAccessException,
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
   *            the target on which to set the value.
   * @param value
   *            the value to set.
   * @throws InvocationTargetException
   *             if the underlying method throws an exception.
   * @throws IllegalAccessException
   *             if this <code>Method</code> object enforces Java language
   *             access control and the underlying method is inaccessible.
   * @throws NoSuchMethodException
   *             if a matching method is not found.
   */
  void setValue(Object target, Object value) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException;
}
