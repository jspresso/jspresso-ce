/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

/**
 * Generic contract of a formatter object.
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
