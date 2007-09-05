/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.exception;

/**
 * This class is for throwing any throwable (checked and unchecked) over
 * non-checked methods.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class NestedRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 611753394832920226L;

  /**
   * Constructs a new NestedRuntimeException.
   * 
   * @param message
   *            the exception message.
   */
  public NestedRuntimeException(String message) {
    super(message);
  }

  /**
   * Constructs a new NestedRuntimeException.
   * 
   * @param nestedException
   *            the wrapped exception (checked or unchecked).
   */
  public NestedRuntimeException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new NestedRuntimeException.
   * 
   * @param nestedException
   *            the wrapped exception (checked or unchecked).
   * @param message
   *            an additional specific message.
   */
  public NestedRuntimeException(Throwable nestedException, String message) {
    super(message, nestedException);
  }
}
