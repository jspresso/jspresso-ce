/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an unexpected exception occurs on a view.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ViewException extends NestedRuntimeException {

  private static final long serialVersionUID = 5403717126687643426L;

  /**
   * Constructs a new <code>ViewException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public ViewException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>ViewException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   */
  public ViewException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>ViewException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   * @param message
   *            the exception message.
   */
  public ViewException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
