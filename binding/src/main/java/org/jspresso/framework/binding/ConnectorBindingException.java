/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding;

import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an exception occurs during connector usage.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @see org.jspresso.framework.binding.IValueConnector#getConnectorValue()
 * @see org.jspresso.framework.binding.IValueConnector#setConnectorValue(Object)
 * @author Vincent Vandenschrick
 */
public class ConnectorBindingException extends NestedRuntimeException {

  private static final long serialVersionUID = 1337226843106009319L;

  /**
   * Constructs a new connector exception.
   * 
   * @param message
   *            the exception message.
   */
  public ConnectorBindingException(String message) {
    super(message);
  }

  /**
   * Constructs a new connector exception.
   * 
   * @param nestedException
   *            the original exception.
   */
  public ConnectorBindingException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new connector exception.
   * 
   * @param nestedException
   *            the original exception.
   * @param message
   *            a specific detail message.
   */
  public ConnectorBindingException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
