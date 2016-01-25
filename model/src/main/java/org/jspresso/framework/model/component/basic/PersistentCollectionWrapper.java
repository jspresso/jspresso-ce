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
package org.jspresso.framework.model.component.basic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.accessor.IListAccessor;

/**
 * Proxy delegate for special collections that delegate their modifiers to their holder interface.
 *
 * @author Vincent Vandenschrick
 */
public class PersistentCollectionWrapper<E> implements InvocationHandler {

  private IComponent         holder;
  private Class<? extends E> elementType;
  private String             propertyName;
  private Collection<E>      wrappedCollection;
  private IAccessorFactory   accessorFactory;

  /**
   * Instantiates a new Persistent collection wrapper.
   *
   * @param wrappedCollection
   *     the wrapped
   * @param holder
   *     the holder
   * @param propertyName
   *     the property name
   * @param elementType
   *     the element type
   * @param accessorFactory
   *     the accessor factory
   */
  public PersistentCollectionWrapper(Collection<E> wrappedCollection, IComponent holder, String propertyName,
                                     Class<? extends E> elementType, IAccessorFactory accessorFactory) {
    this.holder = holder;
    this.wrappedCollection = wrappedCollection;
    this.propertyName = propertyName;
    this.elementType = elementType;
    this.accessorFactory = accessorFactory;
  }

  /**
   * Invoke object.
   *
   * @param proxy
   *     the proxy
   * @param method
   *     the method
   * @param args
   *     the args
   * @return the object
   *
   * @throws Throwable
   *     the throwable
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String methodName = method.getName();
    ICollectionAccessor wrappedAccessor = accessorFactory.createCollectionPropertyAccessor(propertyName,
        holder.getComponentContract(), elementType);
    switch (methodName) {
      case "getWrappedCollection":
        return wrappedCollection;
      case "add":
        if (method.getParameterTypes().length == 1) {
          wrappedAccessor.addToValue(holder, args[0]);
          return true;
        } else if (method.getParameterTypes().length == 2) {
          ((IListAccessor) wrappedAccessor).addToValue(wrappedAccessor, (Integer) args[0], args[1]);
          return null;
        }
        break;
      case "remove":
        if (method.getParameterTypes().length == 1) {
          if (Integer.TYPE.equals(method.getParameterTypes()[0])) {
            Object elementToRemove = ((List) wrappedCollection).get((Integer) args[0]);
            wrappedAccessor.removeFromValue(holder, elementToRemove);
          } else {
            wrappedAccessor.removeFromValue(holder, args[0]);
          }
          return true;
        }
        break;
      default:
        Collection<Object> result;
        if (wrappedCollection instanceof List<?>) {
          result = new ArrayList<>((List<Object>) wrappedCollection);
        } else if (wrappedCollection instanceof Set<?>) {
          result = new LinkedHashSet<>((Set<Object>) wrappedCollection);
        } else {
          throw new ComponentException("Unsupported collection type " + wrappedCollection.getClass().getName());
        }
        Object returnValue = method.invoke(result, args);
        wrappedAccessor.setValue(holder, result);
        return returnValue;
    }
    return method.invoke(wrappedCollection, args);
  }
}
