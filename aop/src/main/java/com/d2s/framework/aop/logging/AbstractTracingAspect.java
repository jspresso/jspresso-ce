/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.aop.logging;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This simple abstract aspect is achieving tracing for the whole application.
 * It relies on Jakarta's commons logging.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * Revision $LastChangedRevision$.
 * 
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTracingAspect {

  private int                callDepth  = 0;
  private Map<Class<?>, Log> loggersMap = new HashMap<Class<?>, Log>();

  /**
   * Logs method entering.
   * 
   * @param clazz
   *            The class (or interface) declaring the method.
   * @param target
   *            The instance on which the method is executed.
   * @param methodName
   *            The name of the method.
   */
  protected void logMethodEntry(final Class<?> clazz, final Object target,
      final String methodName) {
    Log log = getLogger(clazz);
    if (log.isTraceEnabled()) {
      log.trace(indent() + "--> [" + clazz + "::" + methodName
          + "] invoked on " + target);
      increaseCallDepth();
    }
  }

  /**
   * Logs method exiting.
   * 
   * @param clazz
   *            The class (or interface) declaring the method.
   * @param target
   *            The instance on which the method is executed.
   * @param methodName
   *            The name of the method.
   */
  protected void logMethodExit(final Class<?> clazz, final Object target,
      final String methodName) {
    Log log = getLogger(clazz);
    if (log.isTraceEnabled()) {
      decreaseCallDepth();
      log.trace(indent() + "<-- [" + clazz + "::" + methodName
          + "] invoked on " + target);
    }
  }

  private void decreaseCallDepth() {
    callDepth--;
  }

  private Log getLogger(Class<?> clazz) {
    Log logger = loggersMap.get(clazz);
    if (logger == null) {
      logger = LogFactory.getLog(clazz);
      loggersMap.put(clazz, logger);
    }
    return logger;
  }

  private void increaseCallDepth() {
    callDepth++;
  }

  private String indent() {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < callDepth; i++) {
      buff.append("  ");
    }
    return buff.toString();
  }
}
