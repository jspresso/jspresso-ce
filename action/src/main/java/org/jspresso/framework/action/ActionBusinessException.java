/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.action;

import org.jspresso.framework.util.exception.BusinessException;

/**
 * This exception is thrown whenever a business exception occurs on an action.
 *
 * @author Vincent Vandenschrick
 */
public class ActionBusinessException extends BusinessException {

  private static final long serialVersionUID = -6735248989227589846L;

  /**
   * Constructs a new {@code ActionException} instance.
   *
   * @param message
   *          the exception message.
   */
  public ActionBusinessException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code ActionBusinessException} instance.
   *
   * @param message
   *          the exception message.
   * @param staticI18nKey
   *          the static i18n key if any. It will be used by default to get the
   *          internationalized message.
   */
  public ActionBusinessException(String message, String staticI18nKey) {
    super(message, staticI18nKey);
  }

  /**
   * Constructs a new {@code ActionBusinessException} instance.
   *
   * @param message
   *          the exception message.
   * @param staticI18nKey
   *          the static i18n key if any. It will be used by default to get the
   *          internationalized message.
   * @param i18nParams
   *          the parameters of the translated message or null.
   */
  public ActionBusinessException(String message, String staticI18nKey,
      Object... i18nParams) {
    super(message, staticI18nKey, i18nParams);
  }

}
