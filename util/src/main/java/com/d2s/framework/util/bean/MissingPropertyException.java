/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.bean;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever a looked-up property does not exist.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MissingPropertyException extends NestedRuntimeException {

  private static final long serialVersionUID = 802344614276374621L;

  /**
   * Constructs a new <code>MissingPropertyException</code>.
   * 
   * @param message
   *            the exception message.
   */
  public MissingPropertyException(String message) {
    super(message);
  }
}
