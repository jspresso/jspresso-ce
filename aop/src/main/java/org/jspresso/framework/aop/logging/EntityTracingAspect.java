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
import org.codehaus.aspectwerkz.annotation.Expression;
import org.codehaus.aspectwerkz.definition.Pointcut;
import org.codehaus.aspectwerkz.joinpoint.JoinPoint;
import org.codehaus.aspectwerkz.joinpoint.MethodRtti;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.basic.BasicEntityInvocationHandler;


/**
 * This aspect is specifically designed to trace business entity access. As of
 * now, AOP frameworks fail to resolve Joinpoints in runtime generated proxy
 * class.
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
   *            The jointpoint reached by the flow.
   * @return The value returned by the underlying called method.
   * @throws Throwable
   *             Any exception thrown by the underlying method.
   */
  @Around("entityModifiers")
  public Object logMethod(final JoinPoint joinPoint) throws Throwable {
    String accessType = joinPoint.getSignature().getName().substring(0,
        GET_SET_LENGTH);
    MethodRtti rtti = (MethodRtti) joinPoint.getRtti();
    Class<? extends Object> entityClass = ((BasicEntityInvocationHandler) joinPoint
        .getCallee()).getComponentContract();
    Object[] parameterValues = rtti.getParameterValues();
    logMethodEntry(entityClass, parameterValues[0], "[" + accessType + "]"
        + ((IPropertyDescriptor) parameterValues[1]).getName());
    final Object result = joinPoint.proceed();
    logMethodExit(entityClass, parameterValues[0], "[" + accessType + "]"
        + ((IPropertyDescriptor) parameterValues[1]).getName());
    return result;
  }
}
