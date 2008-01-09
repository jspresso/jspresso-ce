/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.launch.ulc;

import java.lang.reflect.InvocationTargetException;

import com.d2s.framework.util.exception.NestedRuntimeException;
import com.ulcjava.base.client.IMessageService;

/**
 * Used to trigger the construction of an instance of a delegate class and to
 * call the method start on it.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
