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
package org.jspresso.framework.util.bean.integrity;

import org.jspresso.framework.util.exception.BusinessException;

/**
 * This exception is thrown whenever an integrity problem occurs on an component
 * modification.
 *
 * @author Vincent Vandenschrick
 */
public class IntegrityException extends BusinessException {

  private static final long serialVersionUID = -5965567517919706757L;

  /**
   * Constructs a new {@code IntegrityException} instance.
   *
   * @param message
   *          the exception message.
   * @param staticI18nKey
   *          the static i18n key if any. It will be used by default to get the
   *          internationalized message.
   */
  public IntegrityException(String message, String staticI18nKey) {
    super(message, staticI18nKey);
  }

  /**
   * Constructs a new {@code IntegrityException} instance.
   *
   * @param message
   *          the exception message.
   * @param staticI18nKey
   *          the static i18n key if any. It will be used by default to get the
   *          internationalized message.
   * @param i18nParams
   *          the parameters of the translated message or null.
   */
  public IntegrityException(String message, String staticI18nKey,
      Object... i18nParams) {
    super(message, staticI18nKey, i18nParams);
  }

  /**
   * Constructs a new {@code IntegrityException} instance.
   *
   * @param message
   *          the exception message.
   */
  protected IntegrityException(String message) {
    super(message);
  }
}
