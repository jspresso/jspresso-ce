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
package org.jspresso.framework.util.exception;

import java.util.Locale;

import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * A "normal" business exception. Whenever a contextual translated message is needed,
 * the method "getI18nMessage" might be overridden.
 *
 * @author Vincent Vandenschrick
 */
public abstract class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -5422600831610337684L;

  private final Object[] i18nParams;
  private final String   staticI18nKey;

  /**
   * Constructs a new {@code BusinessException} instance.
   *
   * @param message
   *     the exception message.
   */
  public BusinessException(String message) {
    this(message, null, (Object[]) null);
  }

  /**
   * Constructs a new {@code BusinessException} instance.
   *
   * @param message
   *     the exception message.
   * @param staticI18nKey
   *     the static i18n key if any. It will be used by default to get the internationalized message.
   */
  public BusinessException(String message, String staticI18nKey) {
    this(message, staticI18nKey, (Object[]) null);
  }

  /**
   * Constructs a new {@code BusinessException} instance.
   *
   * @param message
   *     the exception message.
   * @param staticI18nKey
   *     the static i18n key if any. It will be used by default to get the internationalized message.
   * @param i18nParams
   *     the parameters of the translated message or null.
   */
  public BusinessException(String message, String staticI18nKey,
      Object... i18nParams) {
    super(message);
    this.staticI18nKey = staticI18nKey;
    this.i18nParams = i18nParams;
  }

  /**
   * Gets the exception localized message using a translation provider.
   *
   * @param translationProvider
   *     the translation provider used to translate the exception message.
   * @param locale
   *     the locale to translate the exception to.
   * @return the translated message.
   */
  public String getI18nMessage(ITranslationProvider translationProvider,
      Locale locale) {
    if (i18nParams != null) {
      return translationProvider.getTranslation(staticI18nKey, i18nParams,
          locale);
    }
    return translationProvider
        .getTranslation(staticI18nKey, (String) null, locale);
  }

}
