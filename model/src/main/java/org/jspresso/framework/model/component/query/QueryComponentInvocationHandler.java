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
package org.jspresso.framework.model.component.query;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.bean.AccessorInfo;
import org.jspresso.framework.util.bean.EAccessorType;

/**
 * This is the core implementation of all query components in the application.
 * Instances of this class serve as handlers for proxies representing the query
 * components.
 *
 * @author Vincent Vandenschrick
 */
public class QueryComponentInvocationHandler implements InvocationHandler,
    Serializable {

  private static final long serialVersionUID = 6078989823404409653L;

  private final IComponent        componentDelegate;

  /**
   * Constructs a new {@code QueryComponentInvocationHandler} instance.
   *
   * @param componentDelegate
   *          The component this delegate forwards the method calls to.
   */
  public QueryComponentInvocationHandler(IComponent componentDelegate) {
    this.componentDelegate = componentDelegate;
  }

  /**
   * Handles methods invocations on the entity proxy. Either : <li>delegates to
   * the wrapped entity straightSet method in case of a setter. <li>delegates to
   * the normal method in any other case.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public synchronized Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    if ("getComponentContract".equals(method.getName())) {
      return componentDelegate.getComponentContract();
    }
    AccessorInfo accessorInfo = new AccessorInfo(method);
    EAccessorType accessorType = accessorInfo.getAccessorType();
    if (accessorType == EAccessorType.SETTER) {
      String accessedPropertyName = accessorInfo.getAccessedPropertyName();
      if (accessedPropertyName != null) {
        componentDelegate.straightSetProperty(accessedPropertyName, args[0]);
        return null;
      }
    } else if (accessorType == EAccessorType.GETTER) {
      String accessedPropertyName = accessorInfo.getAccessedPropertyName();
      if (IQueryComponent.QUERIED_COMPONENTS.equals(accessedPropertyName)) {
        return componentDelegate.straightGetProperty(accessedPropertyName);
      }
    }
    return method.invoke(componentDelegate, args);
  }
}
