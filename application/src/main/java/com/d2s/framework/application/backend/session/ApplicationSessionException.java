/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.session;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an unexpected exception occurs on an
 * application session.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ApplicationSessionException extends NestedRuntimeException {

  private static final long serialVersionUID = -809573572653239047L;

  /**
   * Constructs a new <code>ApplicationSessionException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public ApplicationSessionException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>ApplicationSessionException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   */
  public ApplicationSessionException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>ApplicationSessionException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   * @param message
   *            the exception message.
   */
  public ApplicationSessionException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
