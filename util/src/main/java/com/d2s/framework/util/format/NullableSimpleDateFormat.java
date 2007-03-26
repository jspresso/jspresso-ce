/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.format;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple date format that returns null whenever a null or empty string is
 * parsed instead of throwing a ParseException.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class NullableSimpleDateFormat extends SimpleDateFormat {

  private static final long serialVersionUID = 8442150281431602947L;

  /**
   * Constructs a new <code>NullableSimpleDateFormat</code> instance.
   */
  public NullableSimpleDateFormat() {
    super();
  }

  /**
   * Constructs a new <code>NullableSimpleDateFormat</code> instance.
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
   * Constructs a new <code>NullableSimpleDateFormat</code> instance.
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
   * Constructs a new <code>NullableSimpleDateFormat</code> instance.
   * 
   * @param pattern
   *          pattern.
   */
  public NullableSimpleDateFormat(String pattern) {
    super(pattern);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date parse(String source) throws ParseException {
    if (source == null || source.length() == 0) {
      return null;
    }
    return super.parse(source);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parseObject(String source) throws ParseException {
    if (source == null || source.length() == 0) {
      return null;
    }
    return super.parse(source);
  }

}
