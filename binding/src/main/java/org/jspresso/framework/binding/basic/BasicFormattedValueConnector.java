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
package org.jspresso.framework.binding.basic;

import java.text.ParseException;

import org.jspresso.framework.binding.ConnectorBindingException;
import org.jspresso.framework.binding.IFormattedValueConnector;
import org.jspresso.framework.util.format.IFormatter;

/**
 * This is a basic implementation of a formatted value connector built with a
 * formatter.
 * 
 * @version $LastChangedRevision: 1249 $
 * @author Vincent Vandenschrick
 */
public class BasicFormattedValueConnector extends BasicValueConnector implements
    IFormattedValueConnector {

  private IFormatter formatter;

  /**
   * Constructs a new <code>BasicFormattedValueConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param formatter
   *          the format used to parse and format connector value object.
   */
  public BasicFormattedValueConnector(String id, 
      IFormatter formatter) {
    super(id);
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicFormattedValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicFormattedValueConnector clone(String newConnectorId) {
    BasicFormattedValueConnector clonedConnector = (BasicFormattedValueConnector) super
        .clone(newConnectorId);
    return clonedConnector;
  }

  /**
   * Gets the connector value as a string representation.
   * 
   * @return the connector value as a string representation.
   */
  public String getConnectorValueAsString() {
    return getFormatter().format(getConnectorValue());
  }

  /**
   * Sets the connector value as a string representation.
   * 
   * @param valueAsString
   *          the connector value as a string representation.
   */
  public void setConnectorValueAsString(String valueAsString) {
    try {
      setConnectorValue(getFormatter().parse(valueAsString));
    } catch (ParseException ex) {
      throw new ConnectorBindingException(ex);
    }
  }

  /**
   * Gets the formatter.
   * 
   * @return the formatter.
   */
  protected IFormatter getFormatter() {
    return formatter;
  }

}
