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
package org.jspresso.framework.util.format;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * A formatter / parser to deal with duration properties.
 *
 * @author Vincent Vandenschrick
 */
public class DurationFormatter implements IFormatter<Number, String> {

  private final PeriodFormatter formatter;

  /**
   * Constructs a new {@code DurationFormatter} instance.
   *
   * @param translationProvider
   *          the translation provider for duration labels.
   * @param locale
   *          the locale the formatter must be constructed in.
   */
  public DurationFormatter(ITranslationProvider translationProvider,
      Locale locale, boolean secondsAware, boolean millisecondsAware) {
    super();
    PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
    builder.appendDays();
    builder.appendSuffix(
        " " + translationProvider.getTranslation("day", locale), " "
            + translationProvider.getTranslation("days", locale));
    builder.appendSeparator(" ");
    builder.appendHours();
    builder.appendSuffix(
        " " + translationProvider.getTranslation("hour", locale), " "
            + translationProvider.getTranslation("hours", locale));
    builder.appendSeparator(" ");
    builder.appendMinutes();
    builder.appendSuffix(
        " " + translationProvider.getTranslation("minute", locale), " "
            + translationProvider.getTranslation("minutes", locale));
    if (secondsAware) {
      builder.appendSeconds();
      builder.appendSuffix(" " + translationProvider.getTranslation("second", locale),
          " " + translationProvider.getTranslation("seconds", locale));
    }
    if (millisecondsAware) {
      builder.appendMillis();
      builder.appendSuffix(" " + translationProvider.getTranslation("millisecond", locale),
          " " + translationProvider.getTranslation("milliseconds", locale));
    }
    this.formatter = builder.toFormatter().withLocale(locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String format(Number value) {
    if (value == null) {
      return null;
    }
    try {
      return formatter.print(new Period(0, value.longValue()));
    } catch (Throwable t) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Number parse(String source) throws ParseException {
    if (source == null || source.length() == 0) {
      return null;
    }
    try {
      return formatter.parsePeriod(source)
          .toDurationFrom(new Instant(0)).getMillis();
    } catch (Throwable t) {
      throw new ParseException(t.getMessage(), 0);
    }
  }
}
