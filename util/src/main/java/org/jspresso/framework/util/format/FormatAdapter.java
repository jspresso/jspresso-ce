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

import java.text.Format;
import java.text.ParseException;

/**
 * An adapter for the {@code Format} jdk objects.
 *
 * @author Vincent Vandenschrick
 */
public class FormatAdapter implements IFormatter<Object, String> {

  private final Format format;

  /**
   * Constructs a new {@code FormatAdapter} instance.
   *
   * @param format
   *          the format to adapt.
   */
  public FormatAdapter(Format format) {
    super();
    this.format = format;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String format(Object value) {
    if (value != null) {
      return format.format(value);
    }
    return null;
  }

  /**
   * Gets the wrapped format instance.
   *
   * @return the wrapped format instance.
   */
  public Format getFormat() {
    return format;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parse(String source) throws ParseException {
    if (source == null || source.length() == 0) {
      return null;
    }
    return format.parseObject(source);
  }

}
