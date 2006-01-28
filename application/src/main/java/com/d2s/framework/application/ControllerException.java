/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an unexpected exception occurs on a view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerException extends NestedRuntimeException {

  private static final long serialVersionUID = 5403717126687643426L;

  /**
   * Constructs a new <code>ControllerException</code> instance.
   * 
   * @param message
   *          the exception message.
   */
  public ControllerException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>ControllerException</code> instance.
   * 
   * @param nestedException
   *          the nested exception.
   */
  public ControllerException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>ControllerException</code> instance.
   * 
   * @param nestedException
   *          the nested exception.
   * @param message
   *          the exception message.
   */
  public ControllerException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
