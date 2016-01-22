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
package org.jspresso.framework.binding.swing;

import java.text.Format;
import java.text.ParseException;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;

/**
 * JFormattedFieldConnector connector. Instances of this class must be provided
 * with a {@code Format} using the constructor. If not set, the string
 * representation contained in the text component will be used "as is" as the
 * connector value.
 *
 * @author Vincent Vandenschrick
 */
public class JFormattedFieldConnector extends JTextFieldConnector {

  private final IFormatter<?, String> formatter;

  /**
   * Constructs a new {@code JFormattedFieldConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param textField
   *          the connected JTextField.
   * @param format
   *          the j2se format to use to extract the object value.
   */
  public JFormattedFieldConnector(String id, JTextField textField, Format format) {
    this(id, textField, new FormatAdapter(format));
  }

  /**
   * Constructs a new {@code JFormattedFieldConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param textField
   *          the connected JTextField.
   * @param formatter
   *          the formatter to use to extract the object value.
   */
  public JFormattedFieldConnector(String id, JTextField textField,
      IFormatter<?, String> formatter) {
    super(id, textField);
    this.formatter = formatter;
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected Object getConnecteeValue() {
    if (StringUtils.isEmpty(getConnectedJComponent().getText())) {
      return null;
    }
    if (formatter != null) {
      try {
        Object value = formatter
            .parse(getTextForParser(getConnectedJComponent().getText()));
        getConnectedJComponent().setText(
            ((IFormatter<Object, String>) formatter).format(value));
        return value;
      } catch (ParseException ex) {
        setConnecteeValue(null);
        return null;
      }
    }
    return super.getConnecteeValue();
  }

  /**
   * Allows for text reformatting when a non lenient parser is used.
   *
   * @param rawText
   *          the raw text as entered in the text field.
   * @return the text to give to the parser.
   */
  protected String getTextForParser(String rawText) {
    return rawText;
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null || formatter == null) {
      super.protectedSetConnecteeValue(aValue);
    } else {
      getConnectedJComponent().setText(
          ((IFormatter<Object, String>) formatter).format(aValue));
    }
  }
}
