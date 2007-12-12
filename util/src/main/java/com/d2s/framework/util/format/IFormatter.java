/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.format;

import java.text.ParseException;

/**
 * Generic contract of a formatter object.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IFormatter {

  /**
   * Formats an object to it's userfriendly string representation.
   * 
   * @param value
   *            the object to format.
   * @return The formatted string representation.
   */
  String format(Object value);

  /**
   * Parses a string and transform it to an Object value.
   * 
   * @param source
   *            the string representation to parse.
   * @return the parsed object.
   * @throws ParseException
   *             thrown whenever a parsing error ocurred.
   */
  Object parse(String source) throws ParseException;
}
