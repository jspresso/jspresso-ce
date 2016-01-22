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
package org.jspresso.framework.util.i18n;

import java.util.Locale;

/**
 * Abstract base class for {@code ITranslationProvider} implementation.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTranslationProvider implements
    ITranslationProvider {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Locale locale) {
    return getTranslation(key, null, null, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args, Locale locale) {
    return getTranslation(key, args, null, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, String defaultMessage, Locale locale) {
    return getTranslation(key, null, defaultMessage, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDatePattern(Locale locale) {
    return getTranslation(DATE_FORMAT_KEY, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTimePattern(Locale locale) {
    return getTranslation(TIME_FORMAT_KEY, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getShortTimePattern(Locale locale) {
    return getTranslation(TIME_FORMAT_SHORT_KEY, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLongTimePattern(Locale locale) {
    return getTranslation(TIME_FORMAT_LONG_KEY, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFirstDayOfWeek(Locale locale) {
    return Integer.parseInt(getTranslation(FIRST_DAY_OF_WEEK_KEY, Integer.toString(1), locale));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDecimalSeparator(Locale locale) {
    return getTranslation(DECIMAL_SEPARATOR_KEY, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getThousandsSeparator(Locale locale) {
    return getTranslation(THOUSANDS_SEPARATOR_KEY, locale);
  }
}
