/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding;

import java.text.Format;
import java.text.ParseException;

/**
 * This class is an abstract base class for any connector which deals with
 * string representation of its value. Instances of this class must be provided
 * with a <code>Format</code> using the {@link #setFormat(Format)} method. If
 * not set, the string representation will be used "as is" as the connector
 * value.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTextConnector extends AbstractValueConnector {

  private Format format;

  /**
   * Constructs a new <code>AbstractTextConnector</code> instance.
   * 
   * @param id
   *            the connector identifier.
   */
  public AbstractTextConnector(String id) {
    super(id);
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   * 
   * @see #setFormat(Format)
   */
  @Override
  public void setConnecteeValue(Object aValue) {
    setConnectorValueAsString(getFormat().format(aValue));
  }

  /**
   * Sets the format.
   * 
   * @param format
   *            the format to set.
   */
  public void setFormat(Format format) {
    this.format = format;
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   * 
   * @see #setFormat(Format)
   */
  @Override
  protected Object getConnecteeValue() {
    if (getFormat() != null) {
      try {
        return getFormat().parseObject(getConnectorValueAsString());
      } catch (ParseException ex) {
        throw new ConnectorBindingException(ex);
      }
    }
    return getConnectorValueAsString();
  }

  /**
   * Gets the connector value as a string. This string will be parsed to
   * determine the actual connector value.
   * 
   * @return the string representation of the connector value.
   */
  protected abstract String getConnectorValueAsString();

  /**
   * Sets the connector value as a string. This string has been formatted from
   * the actual connector value using the format set using .
   * 
   * @param connectorValueAsString
   *            the string representation of the connector value.
   */
  protected abstract void setConnectorValueAsString(
      String connectorValueAsString);

  /**
   * Gets the format.
   * 
   * @return the format.
   */
  private Format getFormat() {
    return format;
  }
}
