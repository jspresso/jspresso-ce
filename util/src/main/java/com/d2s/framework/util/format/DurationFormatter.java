/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.format;

import java.util.Locale;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;

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
    this.formatter = PeriodFormat.getDefault().withLocale(locale);
  }

  /**
   * {@inheritDoc}
   */
  public Object parse(String source) {
    if (source == null || source.length() == 0) {
      return null;
    }
    return new Long(formatter.parsePeriod(source)
        .toDurationFrom(new Instant(0)).getMillis());
  }

  /**
   * {@inheritDoc}
   */
  public String format(Object value) {
    if (value == null) {
      return null;
    }
    return formatter.print(new Period(0, ((Number) value).longValue()));
  }
}
