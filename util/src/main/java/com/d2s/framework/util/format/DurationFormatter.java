/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.format;

import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * A formatter / parser to deal with duration properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
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
   *          the locale the formatter must be constructed in.
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
}
