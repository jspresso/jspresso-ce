/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.accessor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * This interface is implemented by any bean value accessor on a list property.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IListAccessor extends ICollectionAccessor {

  /**
   * Adds the value to the bean property of this accessor.
   * 
   * @param target
   *            the target on which to add the value.
   * @param index
   *            index at which the specified element is to be inserted.
   * @param value
   *            the value to add.
   * @throws IllegalAccessException
   *             if the underlying method throws an exception.
   * @throws InvocationTargetException
   *             if this <code>Method</code> object enforces Java language
   *             access control and the underlying method is inaccessible.
   * @throws NoSuchMethodException
   *             if a matching method is not found.
   */
  void addToValue(Object target, int index, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException;

  /**
   * Return type refined.
   * <p>
   * {@inheritDoc}
   */
  List<?> getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException;
}
