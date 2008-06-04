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

import org.codehaus.aspectwerkz.annotation.Around;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;

/**
 * A general purpose tracing aspect. Used to debug every "standard" POJO in the
 * framework.
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
