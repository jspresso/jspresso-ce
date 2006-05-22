/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.launch.ulc;

import java.lang.reflect.InvocationTargetException;

import com.d2s.framework.util.exception.NestedRuntimeException;
import com.ulcjava.base.client.IMessageService;

/**
 * Used to trigger the construction of an instance of a delegate class and to
 * call the method start on it.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ClassInvoker implements IMessageService {

  /**
   * <code>MESSAGE_ROOT</code>.
   */
  private static final String MESSAGE_ROOT   = "invokeClass";

  /**
   * <code>SEPARATOR</code>.
   */
  private static final String SEPARATOR      = "::";

  /**
   * <code>ARGS_SEPARATOR</code>.
   */
  private static final String ARGS_SEPARATOR = "§";

  /**
   * {@inheritDoc}
   */
  public void handleMessage(String msg) {
    if (msg.startsWith(MESSAGE_ROOT)) {
      String[] messageParts = msg.split(SEPARATOR);
      try {
        Class targetClass = Class.forName(messageParts[1]);
        String methodName = messageParts[2];
        String[] arguments;
        if (messageParts.length > 3) {
          arguments = messageParts[3].split(ARGS_SEPARATOR);
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
      } catch (InvocationTargetException ex) {
        throw new NestedRuntimeException(ex);
      } catch (NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
  }

  /**
   * Creates a message to be handled by the class invoker.
   * 
   * @param className
   *          the name of the class to trigger.
   * @param methodName
   *          the name of the method to trigger.
   * @param arguments
   *          the arguments to trigger the method.
   * @return the created message.
   */
  public static String createMessage(String className, String methodName,
      String[] arguments) {
    StringBuffer msg = new StringBuffer();
    msg.append(MESSAGE_ROOT);
    msg.append(SEPARATOR);
    msg.append(className);
    msg.append(SEPARATOR);
    msg.append(methodName);
    if (arguments != null && arguments.length > 0) {
      msg.append(SEPARATOR);
      int i = 0;
      for (String argument : arguments) {
        msg.append(argument);
        if (i < arguments.length - 1) {
          msg.append(ARGS_SEPARATOR);
        }
        i++;
      }
    }
    return msg.toString();
  }
}
