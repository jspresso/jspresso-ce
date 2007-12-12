/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * This exception is thrown whenever a binding requires a connector that does
 * not exist.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @see com.d2s.framework.binding.DefaultMvcBinder#bind(IValueConnector,
 *      IValueConnector)
 * @author Vincent Vandenschrick
 */
public class MissingConnectorException extends RuntimeException {

  private static final long serialVersionUID = -2642954716866063359L;

  /**
   * Constructs a new MissingConnectorException with null as its detail message.
   */
  public MissingConnectorException() {
    super();
  }

  /**
   * Constructs a new MissingConnectorException with the specified detail
   * message.
   * 
   * @param message
   *            the detail message.
   */
  public MissingConnectorException(String message) {
    super(message);
  }

}
