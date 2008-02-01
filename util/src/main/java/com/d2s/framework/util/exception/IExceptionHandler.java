/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.exception;

import java.util.Map;

/**
 * This interface establishes the general contract of an object able to
 * intercept and handle exceptions.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IExceptionHandler {

  /**
   * This method is called whenever an exception occurs and has to be handled by
   * the handler.
   * 
   * @param ex
   *            the exception that occurs.
   * @param context
   *            the context where some extra information can be retrieved.
   * @return true if the exception has been fully handled and false if it may
   *         still be propagated.
   */
  boolean handleException(Throwable ex, Map<String, Object> context);
}
