/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity.basic;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.AccessorInfo;

/**
 * This is the core implementation of all query entities in the application.
 * Instances of this class serve as handlers for proxies representing the query
 * entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryEntityInvocationHandler implements InvocationHandler,
    Serializable {

  private static final long serialVersionUID = 6078989823404409653L;

  private IEntity           entityDelegate;

  /**
   * Constructs a new <code>QueryEntityInvocationHandler</code> instance.
   * 
   * @param entityDelegate
   *            The entity this delegate forwards the method calls to.
   */
  QueryEntityInvocationHandler(IEntity entityDelegate) {
    this.entityDelegate = entityDelegate;
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
      return entityDelegate.getContract();
    }
    AccessorInfo accessorInfo = new AccessorInfo(method);
    int accessorType = accessorInfo.getAccessorType();
    if (accessorType == AccessorInfo.SETTER) {
      String accessedPropertyName = accessorInfo.getAccessedPropertyName();
      if (accessedPropertyName != null) {
        entityDelegate.straightSetProperty(accessedPropertyName, args[0]);
        return null;
      }
    } else if (accessorType == AccessorInfo.GETTER) {
      String accessedPropertyName = accessorInfo.getAccessedPropertyName();
      if (accessedPropertyName != null) {
        return entityDelegate.straightGetProperty(accessedPropertyName);
      }
    }
    return method.invoke(entityDelegate, args);
  }
}
