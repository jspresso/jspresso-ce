/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean.integrity;

import org.jspresso.framework.util.exception.BusinessException;

/**
 * This exception is thrown whenever an integrity problem occurs on an component
 * modification.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class IntegrityException extends BusinessException {

  private static final long serialVersionUID = -5965567517919706757L;

  /**
   * Constructs a new <code>IntegrityException</code> instance.
   * 
   * @param message
   *            the exception message.
   * @param staticI18nKey
   *            the static i18n key if any. It will be used by default to get
   *            the internationalized message.
   */
  public IntegrityException(String message, String staticI18nKey) {
    super(message, staticI18nKey);
  }

  /**
   * Constructs a new <code>IntegrityException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  protected IntegrityException(String message) {
    super(message);
  }
}
