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
package org.jspresso.framework.binding.wings;

import java.text.Format;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;
import org.wings.STextField;


/**
 * SFormattedFieldConnector connector. Instances of this class must be provided
 * with a <code>Format</code> using the constructor. If not set, the string
 * representation contained in the text component will be used "as is" as the
 * connector value.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SFormattedFieldConnector extends STextFieldConnector {

  private IFormatter formatter;

  /**
   * Constructs a new <code>SFormattedFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected STextField.
   * @param format
   *            the j2se format to use to extract the object value.
   */
  public SFormattedFieldConnector(String id, STextField textField, Format format) {
    this(id, textField, new FormatAdapter(format));
  }

  /**
   * Constructs a new <code>SFormattedFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected STextField.
   * @param formatter
   *            the formatter to use to extract the object value.
   */
  public SFormattedFieldConnector(String id, STextField textField,
      IFormatter formatter) {
    super(id, textField);
    this.formatter = formatter;
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    if (StringUtils.isEmpty(getConnectedSComponent().getText())) {
      return null;
    }
    if (formatter != null) {
      try {
        Object value = formatter
            .parse(getTextForParser(getConnectedSComponent().getText()));
        getConnectedSComponent().setText(formatter.format(value));
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
   *            the raw text as entered in the textfield.
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
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null || formatter == null) {
      super.setConnecteeValue(aValue);
    } else {
      getConnectedSComponent().setText(formatter.format(aValue));
    }
  }
}
