/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an unexpected exception occurs on an
 * entity.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComponentException extends NestedRuntimeException {

  private static final long serialVersionUID = -3659844614276359719L;

  /**
   * Constructs a new <code>EntityException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public ComponentException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>EntityException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   */
  public ComponentException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>EntityException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   * @param message
   *            the exception message.
   */
  public ComponentException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
