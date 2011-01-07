/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.launch.ulc;

import java.lang.reflect.InvocationTargetException;

import org.jspresso.framework.util.exception.NestedRuntimeException;

import com.ulcjava.base.client.IMessageService;

/**
 * Used to trigger the construction of an instance of a delegate class and to
 * call the method start on it.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ClassInvoker implements IMessageService {

  /**
   * {@inheritDoc}
   */
  public void handleMessage(String msg) {
    if (msg.startsWith(ClassInvokerUtil.MESSAGE_ROOT)) {
      String[] messageParts = msg.split(ClassInvokerUtil.SEPARATOR);
      try {
        Class<?> targetClass = Class.forName(messageParts[1]);
        String methodName = messageParts[2];
        String[] arguments;
        if (messageParts.length > 3) {
          arguments = messageParts[3].split(ClassInvokerUtil.ARGS_SEPARATOR);
        } else {
          arguments = new String[0];
        }

        targetClass.getMethod(methodName, new Class[] {String[].class}).invoke(
            targetClass, new Object[] {arguments});
      } catch (ClassNotFoundException ex) {
        throw new NestedRuntimeException(ex);
      } catch (IllegalArgumentException ex) {
        throw new NestedRuntimeException(ex);
      } catch (SecurityException ex) {
        throw new NestedRuntimeException(ex);
      } catch (IllegalAccessException ex) {
        throw new NestedRuntimeException(ex);
      } catch (NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex);
      } catch (InvocationTargetException ex) {
        // ignored
        ex.printStackTrace();
      }
    }
  }
}
