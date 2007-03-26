/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import com.d2s.framework.model.component.ComponentException;

/**
 * This exception is thrown whenever an unexpected exception occurs on an
 * entity.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityException extends ComponentException {

  private static final long serialVersionUID = -3659844614276359719L;

  /**
   * Constructs a new <code>EntityException</code> instance.
   * 
   * @param message
   *          the exception message.
   */
  public EntityException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>EntityException</code> instance.
   * 
   * @param nestedException
   *          the nested exception.
   */
  public EntityException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>EntityException</code> instance.
   * 
   * @param nestedException
   *          the nested exception.
   * @param message
   *          the exception message.
   */
  public EntityException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
