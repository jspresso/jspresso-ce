/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.ldap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a simple helper class to be able to cope with ldap.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class LdapUtils {

  private LdapUtils() {
    // private constructor for helper class.
  }

  /**
   * Formats an boolean attribute.
   * 
   * @param value
   *            the boolean value.
   * @return the parsed boolean.
   */
  public static String formatBoolean(boolean value) {
    return new Boolean(value).toString().toUpperCase();
  }

  /**
   * Formats an integer attribute.
   * 
   * @param value
   *            the integer value.
   * @return the parsed integer or null.
   */
  public static String formatInteger(Integer value) {
    if (value != null) {
      return value.toString();
    }
    return null;
  }

  /**
   * Parses an boolean attribute.
   * 
   * @param booleanAsString
   *            the string representation of the boolean.
   * @return the parsed boolean or null.
   */
  public static boolean parseBoolean(String booleanAsString) {
    if (booleanAsString != null) {
      return Boolean.parseBoolean(booleanAsString);
    }
    return false;
  }

  /**
   * Parses a generalized time as a date ignoring hour minute seconds.
   * 
   * @param generalizedTime
   *            the string representation of the date/time in the format
   *            YYYYMMDDHHMMSS.
   * @return the parsed date or null.
   * @throws ParseException
   *             Whenever a parse exception occurs.
   */
  public static Date parseGeneralizedTimeAsDate(String generalizedTime)
      throws ParseException {
    if (generalizedTime != null) {
      return new SimpleDateFormat("yyyyMMdd").parse(generalizedTime.substring(
          0, 8));
    }
    return null;
  }

  /**
   * Parses a generalized time as a date and time.
   * 
   * @param generalizedTime
   *            the string representation of the date/time in the format
   *            YYYYMMDDHHMMSS.
   * @return the parsed date or null.
   * @throws ParseException
   *             Whenever a parse exception occurs.
   */
  public static Date parseGeneralizedTimeAsDateTime(String generalizedTime)
      throws ParseException {
    if (generalizedTime != null) {
      return new SimpleDateFormat("yyyyMMddHHmmss").parse(generalizedTime
          .substring(0, 12));
    }
    return null;
  }

  /**
   * Parses an integer attribute.
   * 
   * @param integerAsString
   *            the string representation of the integer.
   * @return the parsed integer or null.
   */
  public static Integer parseInteger(String integerAsString) {
    if (integerAsString != null) {
      return new Integer(integerAsString);
    }
    return null;
  }
}
