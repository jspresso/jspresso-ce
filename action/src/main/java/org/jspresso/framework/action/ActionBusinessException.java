/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.action;

import org.jspresso.framework.util.exception.BusinessException;

/**
 * This exception is thrown whenever a business exception occurs on an action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionBusinessException extends BusinessException {

  private static final long serialVersionUID = -6735248989227589846L;

  /**
   * Constructs a new <code>ActionException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public ActionBusinessException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>ActionBusinessException</code> instance.
   * 
   * @param message
   *            the exception message.
   * @param staticI18nKey
   *            the static i18n key if any. It will be used by default to get
   *            the internationalized message.
   */
  public ActionBusinessException(String message, String staticI18nKey) {
    super(message, staticI18nKey);
  }
}
