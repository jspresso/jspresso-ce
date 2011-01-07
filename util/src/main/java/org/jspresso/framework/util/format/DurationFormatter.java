/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
import java.util.ResourceBundle;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * A formatter / parser to deal with duration properties.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DurationFormatter implements IFormatter {

  private PeriodFormatter formatter;

  /**
   * Constructs a new <code>DurationFormatter</code> instance.
   * 
   * @param locale
   *            the locale the formatter must be constructed in.
   */
  public DurationFormatter(Locale locale) {
    super();
    ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName(),
        locale);
    PeriodFormatterBuilder builder = new PeriodFormatterBuilder();
    builder.appendDays();
    builder.appendSuffix(" " + bundle.getString("day"), " "
        + bundle.getString("days"));
    builder.appendSeparator(" ");
    builder.appendHours();
    builder.appendSuffix(" " + bundle.getString("hour"), " "
        + bundle.getString("hours"));
    builder.appendSeparator(" ");
    builder.appendMinutes();
    builder.appendSuffix(" " + bundle.getString("minute"), " "
        + bundle.getString("minutes"));
    this.formatter = builder.toFormatter().withLocale(locale);
  }

  /**
   * {@inheritDoc}
   */
  public String format(Object value) {
    if (value == null) {
      return null;
    }
    try {
      return formatter.print(new Period(0, ((Number) value).longValue()));
    } catch (Throwable t) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object parse(String source) throws ParseException {
    if (source == null || source.length() == 0) {
      return null;
    }
    try {
      return new Long(formatter.parsePeriod(source).toDurationFrom(
          new Instant(0)).getMillis());
    } catch (Throwable t) {
      throw new ParseException(t.getMessage(), 0);
    }
  }
}
