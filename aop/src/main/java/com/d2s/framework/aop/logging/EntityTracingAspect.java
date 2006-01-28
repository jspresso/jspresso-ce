/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.aop.logging;

import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.annotation.Expression;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;

import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.entity.basic.BasicEntityInvocationHandler;

/**
 * This aspect is specifically designed to trace business entity access. As of
 * now, AOP frameworks fail to resolve Joinpoints in runtime generated proxy
 * class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityTracingAspect extends AbstractTracingAspect {

  private static final int GET_SET_LENGTH = 3;

  @Expression("execution(* ..EntityInvocationHandler.setProperty(..))"
      + "||execution(* ..EntityInvocationHandler.addToProperty(..))"
      + "||execution(* ..EntityInvocationHandler.removeFromProperty(..))")
  @SuppressWarnings("unused")
  private Pointcut         entityModifiers;

  /**
   * The main advice used to trace modifier calls on entities.
   * 
   * @param joinPoint
   *          The jointpoint reached by the flow.
   * @return The value returned by the underlying called method.
   * @throws Throwable
   *           Any exception thrown by the underlying method.
   */
  @Around("entityModifiers")
  public Object logMethod(final JoinPoint joinPoint) throws Throwable {
    String accessType = joinPoint.getSignature().getName().substring(0,
        GET_SET_LENGTH);
    MethodRtti rtti = (MethodRtti) joinPoint.getRtti();
    Class entityClass = ((BasicEntityInvocationHandler) joinPoint.getCallee())
        .getEntityContract();
    Object[] parameterValues = rtti.getParameterValues();
    logMethodEntry(entityClass, parameterValues[0], "[" + accessType + "]"
        + ((IPropertyDescriptor) parameterValues[1]).getName());
    final Object result = joinPoint.proceed();
    logMethodExit(entityClass, parameterValues[0], "[" + accessType + "]"
        + ((IPropertyDescriptor) parameterValues[1]).getName());
    return result;
  }
}
