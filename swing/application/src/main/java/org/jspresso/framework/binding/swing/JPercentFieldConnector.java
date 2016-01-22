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

import javax.swing.JTextField;

import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;

/**
 * Automatically adds a '%' character at the end of the passed string if needed.
 *
 * @author Vincent Vandenschrick
 */
public class JPercentFieldConnector extends JFormattedFieldConnector {

  private static final String PERCENT = "%";

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
  public JPercentFieldConnector(String id, JTextField textField, Format format) {
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
  public JPercentFieldConnector(String id, JTextField textField,
      IFormatter<?, String> formatter) {
    super(id, textField, formatter);
  }

  /**
   * Automatically adds a '%' character at the end of the passed string if
   * needed.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getTextForParser(String rawText) {
    String textToParse = rawText;
    if (!textToParse.endsWith(PERCENT)) {
      textToParse = textToParse + PERCENT;
    }
    return textToParse;
  }
}
