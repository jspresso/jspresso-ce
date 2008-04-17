/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.launch.ulc;

/**
 * Helper class used to construct a class invoker message and hold constants.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision: 959 $
 * @author Vincent Vandenschrick
 */
public final class ClassInvokerUtil {

  /**
   * <code>ARGS_SEPARATOR</code>.
   */
  static final String ARGS_SEPARATOR = "§";

  /**
   * <code>MESSAGE_ROOT</code>.
   */
  static final String MESSAGE_ROOT   = "invokeClass";

  /**
   * <code>SEPARATOR</code>.
   */
  static final String SEPARATOR      = "::";

  /**
   * Constructs a new <code>ClassInvokerUtil</code> instance.
   */
  private ClassInvokerUtil() {
    // Helper class constructor.
  }

  /**
   * Creates a message to be handled by the class invoker.
   * 
   * @param className
   *            the name of the class to trigger.
   * @param methodName
   *            the name of the method to trigger.
   * @param arguments
   *            the arguments to trigger the method.
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
