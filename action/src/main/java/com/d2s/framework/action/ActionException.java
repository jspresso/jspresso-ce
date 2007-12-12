/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.action;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an unexpected exception occurs on an
 * action.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionException extends NestedRuntimeException {

  private static final long serialVersionUID = 5403717126687643426L;

  /**
   * Constructs a new <code>ActionException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public ActionException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>ActionException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   */
  public ActionException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>ActionException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   * @param message
   *            the exception message.
   */
  public ActionException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
