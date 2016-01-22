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
package org.jspresso.framework.model.component.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base abstract implementation of a service delegate. It holds a thread local
 * in order to store the current target component this delegate is attached to.
 *
 * @author Vincent Vandenschrick
 * @param <T>
 *          the actual target component type.
 */
public class AbstractComponentServiceDelegate<T> implements IComponentService {

  private final ThreadLocal<T> targetStore = new ThreadLocal<>();

  /**
   * Gets the target component this service delegate is attached to.
   *
   * @return the target component this service delegate is attached to.
   */
  protected T getComponent() {
    return targetStore.get();
  }

  /**
   * Executes a service method for a given target.
   *
   * @param target
   *          the component target to execute the service method for.
   * @param serviceMethod
   *          the service method to execute.
   * @param args
   *          the service arguments.
   * @return the service method result.
   * @throws InvocationTargetException
   *           whenever an unexpected exception occurs when invoking the method.
   * @throws IllegalAccessException
   *           whenever an unexpected exception occurs when invoking the method.
   */
  public Object executeWith(T target, Method serviceMethod, Object... args)
      throws IllegalAccessException, InvocationTargetException {
    T originalTarget = targetStore.get();
    try {
      targetStore.set(target);
      return serviceMethod.invoke(this, args);
    } finally {
      targetStore.set(originalTarget);
    }
  }
}
