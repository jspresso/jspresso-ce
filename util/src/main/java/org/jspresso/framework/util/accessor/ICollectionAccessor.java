/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.accessor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * This interface is implemented by any bean value accessor on a collection
 * property.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICollectionAccessor extends IAccessor {

  /**
   * Adds the value to the bean property of this accessor.
   * 
   * @param target
   *            the target on which to add the value.
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
  void addToValue(Object target, Object value) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException;

  /**
   * Return type refined.
   * <p>
   * {@inheritDoc}
   */
  Collection<?> getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException;

  /**
   * Removes the value from the bean property of this accessor.
   * 
   * @param target
   *            the target on which to remove the value.
   * @param value
   *            the value to remove.
   * @throws IllegalAccessException
   *             if the underlying method throws an exception.
   * @throws InvocationTargetException
   *             if this <code>Method</code> object enforces Java language
   *             access control and the underlying method is inaccessible.
   * @throws NoSuchMethodException
   *             if a matching method is not found.
   */
  void removeFromValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException;
}
