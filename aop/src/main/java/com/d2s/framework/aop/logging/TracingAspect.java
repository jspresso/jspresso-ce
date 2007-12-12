/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.aop.logging;

import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * A general purpose tracing aspect. Used to debug every "standard" POJO in the
 * framework.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * Revision $LastChangedRevision$.
 * 
 * @author Vincent Vandenschrick
 */
public class TracingAspect extends AbstractTracingAspect {

  /**
   * The main advice used to trace methods on POJOs.
   * 
   * @param joinPoint
   *            The jointpoint reached by the flow.
   * @return The value returned by the underlying called method.
   * @throws Throwable
   *             Any exception thrown by the underlying method.
   */
  @Around("methodsToTrace")
  public Object logMethod(final JoinPoint joinPoint) throws Throwable {
    logMethodEntry(joinPoint.getCalleeClass(), joinPoint.getCallee(), joinPoint
        .getSignature().getName());
    final Object result = joinPoint.proceed();
    logMethodExit(joinPoint.getCalleeClass(), joinPoint.getCallee(), joinPoint
        .getSignature().getName());
    return result;
  }
}
