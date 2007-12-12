/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an unexpected exception occurs on an entity
 * registry.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityRegistryException extends NestedRuntimeException {

  private static final long serialVersionUID = -2305354369001291978L;

  /**
   * Constructs a new <code>EntityRegistryException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public EntityRegistryException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>EntityRegistryException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   */
  public EntityRegistryException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>EntityRegistryException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   * @param message
   *            the exception message.
   */
  public EntityRegistryException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
