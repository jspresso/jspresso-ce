/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component.query;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.bean.AccessorInfo;


/**
 * This is the core implementation of all query components in the application.
 * Instances of this class serve as handlers for proxies representing the query
 * components.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryComponentInvocationHandler implements InvocationHandler,
    Serializable {

  private static final long serialVersionUID = 6078989823404409653L;

  private IComponent        componentDelegate;

  /**
   * Constructs a new <code>QueryComponentInvocationHandler</code> instance.
   * 
   * @param componentDelegate
   *            The component this delegate forwards the method calls to.
   */
  public QueryComponentInvocationHandler(IComponent componentDelegate) {
    this.componentDelegate = componentDelegate;
  }

  /**
   * Handles methods invocations on the entity proxy. Either :
   * <li>delegates to the wrapped entity straightSet method in case of a
   * setter.
   * <li>delegates to the normal method in any other case.
   * <p>
   * {@inheritDoc}
   */
  public synchronized Object invoke(@SuppressWarnings("unused")
  Object proxy, Method method, Object[] args) throws Throwable {
    if ("getContract".equals(method.getName())) {
      return componentDelegate.getContract();
    }
    AccessorInfo accessorInfo = new AccessorInfo(method);
    int accessorType = accessorInfo.getAccessorType();
    if (accessorType == AccessorInfo.SETTER) {
      String accessedPropertyName = accessorInfo.getAccessedPropertyName();
      if (accessedPropertyName != null) {
        componentDelegate.straightSetProperty(accessedPropertyName, args[0]);
        return null;
      }
    } else if (accessorType == AccessorInfo.GETTER) {
      String accessedPropertyName = accessorInfo.getAccessedPropertyName();
      if (IQueryComponent.QUERIED_COMPONENTS.equals(accessedPropertyName)) {
        if (accessedPropertyName != null) {
          return componentDelegate.straightGetProperty(accessedPropertyName);
        }
      }
    }
    return method.invoke(componentDelegate, args);
  }
}
