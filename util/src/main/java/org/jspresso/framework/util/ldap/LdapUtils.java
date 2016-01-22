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
package org.jspresso.framework.util.ldap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is a simple helper class to be able to cope with ldap.
 *
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
   *          the boolean value.
   * @return the parsed boolean.
   */
  public static String formatBoolean(boolean value) {
    return Boolean.valueOf(value).toString().toUpperCase();
  }

  /**
   * Formats an integer attribute.
   *
   * @param value
   *          the integer value.
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
   *          the string representation of the boolean.
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
   *          the string representation of the date/time in the format
   *          YYYYMMDDHHMMSS.
   * @return the parsed date or null.
   * @throws ParseException
   *           Whenever a parse exception occurs.
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
   *          the string representation of the date/time in the format
   *          YYYYMMDDHHMMSS.
   * @return the parsed date or null.
   * @throws ParseException
   *           Whenever a parse exception occurs.
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
   *          the string representation of the integer.
   * @return the parsed integer or null.
   */
  public static Integer parseInteger(String integerAsString) {
    if (integerAsString != null) {
      return Integer.valueOf(integerAsString);
    }
    return null;
  }
}
