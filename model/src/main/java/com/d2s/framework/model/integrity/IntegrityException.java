/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.integrity;

/**
 * This exception is thrown whenever an integrity problem occurs on an component
 * modification.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class IntegrityException extends Exception {

  private static final long serialVersionUID = -5444574277286334644L;

  /**
   * Constructs a new <code>IntegrityException</code> instance.
   */
  public IntegrityException() {
    super();
  }

  /**
   * Constructs a new <code>IntegrityException</code> instance.
   * 
   * @param message
   *          the exception message describing the integrity problem.
   */
  public IntegrityException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>IntegrityException</code> instance.
   * 
   * @param cause
   *          the nested exception which caused the integrity problem.
   */
  public IntegrityException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new <code>IntegrityException</code> instance.
   * 
   * @param message
   *          the exception message describing the integrity problem.
   * @param cause
   *          the nested exception which caused the integrity problem.
   */
  public IntegrityException(String message, Throwable cause) {
    super(message, cause);
  }

}
