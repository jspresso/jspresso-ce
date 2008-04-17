/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.scripting;

import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * This exception is thrown whenever an unexpected exception occurs on a view.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ScriptException extends NestedRuntimeException {

  private static final long serialVersionUID = 5403717126687643426L;

  /**
   * Constructs a new <code>ScriptException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public ScriptException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>ScriptException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   */
  public ScriptException(Throwable nestedException) {
    super(nestedException);
  }

  /**
   * Constructs a new <code>ScriptException</code> instance.
   * 
   * @param nestedException
   *            the nested exception.
   * @param message
   *            the exception message.
   */
  public ScriptException(Throwable nestedException, String message) {
    super(nestedException, message);
  }
}
