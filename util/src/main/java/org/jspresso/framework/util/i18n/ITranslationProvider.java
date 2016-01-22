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
 * The interface being implemented by all i18n string providers.
 *
 * @author Vincent Vandenschrick
 */
public interface ITranslationProvider {

  /**
   * {@code TIME_FORMAT_SHORT_KEY}.
   */
  String TIME_FORMAT_SHORT_KEY = "time_format_short";
  /**
   * {@code TIME_FORMAT_LONG_KEY}.
   */
  String TIME_FORMAT_LONG_KEY = "time_format_long";
  /**
   * {@code TIME_FORMAT_KEY}.
   */
  String TIME_FORMAT_KEY       = "time_format";
  /**
   * {@code DATE_FORMAT_KEY}.
   */
  String DATE_FORMAT_KEY       = "date_format";

  /**
   * {@code FIRST_DAY_OF_WEEK_KEY}.
   */
  String FIRST_DAY_OF_WEEK_KEY       = "firstDayOfWeek";

  /**
   * {@code DECIMAL_SEPARATOR_KEY}.
   */
  String DECIMAL_SEPARATOR_KEY = "decimal_separator";

  /**
   * {@code THOUSANDS_SEPARATOR_KEY}.
   */
  String THOUSANDS_SEPARATOR_KEY = "thousands_separator";

  /**
   * Gets a translated string based on a key.
   *
   * @param key
   *          the i18n key.
   * @param locale
   *          the locale the string must be translated into.
   * @return the translated string.
   */
  String getTranslation(String key, Locale locale);

  /**
   * Gets a translated string based on a key using a default message when not
   * found.
   *
   * @param key
   *          the i18n key.
   * @param defaultMessage
   *          the default message to use whenever the key is not found.
   * @param locale
   *          the locale the string must be translated into.
   * @return the translated string.
   */
  String getTranslation(String key, String defaultMessage, Locale locale);

  /**
   * Gets a translated message based on a key.
   *
   * @param key
   *          the i18n key.
   * @param args
   *          the message arguments used in message format.
   * @param locale
   *          the locale the string must be translated into.
   * @return the translated string.
   */
  String getTranslation(String key, Object[] args, Locale locale);

  /**
   * Gets a translated message based on a key using a default message when not
   * found.
   *
   * @param key
   *          the i18n key.
   * @param args
   *          the message arguments used in message format.
   * @param defaultMessage
   *          the default message to use whenever the key is not found.
   * @param locale
   *          the locale the string must be translated into.
   * @return the translated string.
   */
  String getTranslation(String key, Object[] args, String defaultMessage,
      Locale locale);

  /**
   * Return the default date pattern expressed as a SimpleDateFormat pattern.
   *
   * @param locale
   *          the locale.
   * @return the default date pattern.
   */
  String getDatePattern(Locale locale);

  /**
   * Return the default first day of week as 0 for sunday to 6 for saturday.
   *
   * @param locale
   *          the locale.
   * @return the default first day of week.
   */
  int getFirstDayOfWeek(Locale locale);

  /**
   * Return the default time pattern (including seconds) expressed as a
   * SimpleDateFormat pattern.
   *
   * @param locale
   *          the locale.
   * @return the default short time pattern.
   */
  String getTimePattern(Locale locale);

  /**
   * Return the default long time pattern (including seconds and milliseconds) expressed as a
   * SimpleDateFormat pattern.
   *
   * @param locale
   *     the locale.
   * @return the default short time pattern.
   */
  String getLongTimePattern(Locale locale);

  /**
   * Return the default short time pattern (without seconds) expressed as a
   * SimpleDateFormat pattern.
   *
   * @param locale
   *          the locale.
   * @return the default short time pattern.
   */
  String getShortTimePattern(Locale locale);

  /**
   * Return the default default decimal separator.
   *
   * @param locale
   *          the locale.
   * @return the default decimal separator.
   */
  String getDecimalSeparator(Locale locale);


  /**
   * Return the default default thousands separator.
   *
   * @param locale
   *          the locale.
   * @return the default thousands separator.
   */
  String getThousandsSeparator(Locale locale);
}
