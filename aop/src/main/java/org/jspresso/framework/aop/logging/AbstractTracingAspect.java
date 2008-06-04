/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.aop.logging;

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
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
