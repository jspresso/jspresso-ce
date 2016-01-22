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

import java.util.Map;

/**
 * A formatter for translated enumerations. Does not support parsing.
 *
 * @author Vincent Vandenschrick
 */
public class EnumerationFormatter implements IFormatter<Object, String> {

  private final Map<Object, String> values;

  /**
   * Constructs a new {@code EnumFormatter} instance.
   *
   * @param values
   *          the values translations. A null translation map simply means that
   *          the values are returned as is.
   */
  public EnumerationFormatter(Map<Object, String> values) {
    this.values = values;
  }

  /**
   * Performs a lookup of the value in the translation map.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String format(Object value) {
    if (value == null) {
      return "";
    }
    if (values != null && values.containsKey(value)) {
      return values.get(value);
    }
    return value.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parse(String source) {
    throw new UnsupportedOperationException(
        "EnumFormatter is a 1-way formatter");
  }

}
