/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.exception;

import java.util.Map;

/**
 * This interface establishes the general contract of an object able to
 * intercept and handle exceptions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
   *          the exception that occurs.
   * @param context
   *          the context where some extra information can be retrieved.
   */
  void handleException(Throwable ex, Map<String, Object> context);
}
