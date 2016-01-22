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

/**
 * Generic contract of a formatter object.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the source type.
 * @param <F>
 *          the destination type.
 */
public interface IFormatter<E, F> {

  /**
   * Formats an object to another representation, generally a string.
   *
   * @param value
   *          the object to format.
   * @return The formatted string representation.
   */
  F format(E value);

  /**
   * Parses an external representation (generally a string) and transform it to
   * an Object value.
   *
   * @param source
   *          the string representation to parse.
   * @return the parsed object.
   * @throws ParseException
   *           thrown whenever a parsing error occurred.
   */
  E parse(F source) throws ParseException;
}
