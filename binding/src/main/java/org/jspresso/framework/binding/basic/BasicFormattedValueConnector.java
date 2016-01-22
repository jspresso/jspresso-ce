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
package org.jspresso.framework.binding.basic;

import java.text.ParseException;

import org.jspresso.framework.binding.IFormattedValueConnector;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.format.ParsingException;

/**
 * This is a basic implementation of a formatted value connector built with a
 * formatter.
 *
 * @author Vincent Vandenschrick
 */
public class BasicFormattedValueConnector extends BasicValueConnector implements
    IFormattedValueConnector {

  private final IFormatter<Object, Object> formatter;

  /**
   * Constructs a new {@code BasicFormattedValueConnector} instance.
   *
   * @param id
   *          the connector id.
   * @param formatter
   *          the format used to parse and format connector value object.
   */
  @SuppressWarnings("unchecked")
  public BasicFormattedValueConnector(String id, IFormatter<?, ?> formatter) {
    super(id);
    this.formatter = (IFormatter<Object, Object>) formatter;
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
  @Override
  public Object getFormattedValue() {
    Object formattedValue = getFormatter().format(getConnectorValue());
    return formattedValue;
  }

  /**
   * Sets the connector value using the formatted value representation.
   *
   * @param formattedValue
   *          the formatted value, generally the string representation string
   *          representation.
   */
  @Override
  public void setFormattedValue(Object formattedValue) {
    try {
      Object parsedValue = getFormatter().parse(formattedValue);
      setConnectorValue(parsedValue);
    } catch (ParseException ex) {
      // To force resetting the view
      fireValueChange(new ValueChangeEvent(this,
          IPropertyChangeCapable.UNKNOWN, getConnecteeValue()));
      Object[] i18nParams = {
        formattedValue
      };
      handleException(new ParsingException(formattedValue
          + " cannot be parsed.", "error.parsing", i18nParams));
    }
  }

  /**
   * Gets the formatter.
   *
   * @return the formatter.
   */
  protected IFormatter<Object, Object> getFormatter() {
    return formatter;
  }

}
