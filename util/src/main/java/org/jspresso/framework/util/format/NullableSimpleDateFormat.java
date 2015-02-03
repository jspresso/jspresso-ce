/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.format;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple date format that returns null whenever a null or empty string is
 * parsed instead of throwing a ParseException.
 * 
 * @author Vincent Vandenschrick
 */
public class NullableSimpleDateFormat extends SimpleDateFormat {

  private static final long serialVersionUID = 8442150281431602947L;

  /**
   * Constructs a new {@code NullableSimpleDateFormat} instance.
   */
  public NullableSimpleDateFormat() {
    super();
  }

  /**
   * Constructs a new {@code NullableSimpleDateFormat} instance.
   *
   * @param pattern
   *          pattern.
   */
  public NullableSimpleDateFormat(String pattern) {
    super(pattern);
  }

  /**
   * Constructs a new {@code NullableSimpleDateFormat} instance.
   *
   * @param pattern
   *          pattern.
   * @param formatSymbols
   *          formatSymbols.
   */
  public NullableSimpleDateFormat(String pattern,
      DateFormatSymbols formatSymbols) {
    super(pattern, formatSymbols);
  }

  /**
   * Constructs a new {@code NullableSimpleDateFormat} instance.
   *
   * @param pattern
   *          pattern.
   * @param locale
   *          locale.
   */
  public NullableSimpleDateFormat(String pattern, Locale locale) {
    super(pattern, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date parse(String source) throws ParseException {
    if (source == null || source.length() == 0) {
      return null;
    }
    try {
      return super.parse(source);
    } catch (ParseException pe) {
      if (pe.getErrorOffset() == source.length()) {
        try {
          String originalPattern = toPattern();
          String reducedPattern = originalPattern.substring(0,
              pe.getErrorOffset());
          Date incomplete = new SimpleDateFormat(reducedPattern).parse(source);
          Calendar cal = Calendar.getInstance();
          cal.setTime(incomplete);
          Calendar now = Calendar.getInstance();
          if (originalPattern.contains("y") && !reducedPattern.contains("y")) {
            cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
          }
          if (originalPattern.contains("M") && !reducedPattern.contains("M")) {
            cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
          }
          if (originalPattern.contains("d") && !reducedPattern.contains("d")) {
            cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
          }
          return cal.getTime();
        } catch (ParseException nestedPe) {
          throw pe;
        }
      }
      throw pe;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parseObject(String source) throws ParseException {
    return parse(source);
  }

}
