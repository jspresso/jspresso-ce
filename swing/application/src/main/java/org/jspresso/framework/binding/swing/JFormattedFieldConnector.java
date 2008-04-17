/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import java.text.Format;
import java.text.ParseException;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;


/**
 * JFormattedFieldConnector connector. Instances of this class must be provided
 * with a <code>Format</code> using the constructor. If not set, the string
 * representation contained in the text component will be used "as is" as the
 * connector value.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JFormattedFieldConnector extends JTextFieldConnector {

  private IFormatter formatter;

  /**
   * Constructs a new <code>JFormattedFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected JTextField.
   * @param format
   *            the j2se format to use to extract the object value.
   */
  public JFormattedFieldConnector(String id, JTextField textField, Format format) {
    this(id, textField, new FormatAdapter(format));
  }

  /**
   * Constructs a new <code>JFormattedFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected JTextField.
   * @param formatter
   *            the formatter to use to extract the object value.
   */
  public JFormattedFieldConnector(String id, JTextField textField,
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
    if (StringUtils.isEmpty(getConnectedJComponent().getText())) {
      return null;
    }
    if (formatter != null) {
      try {
        Object value = formatter
            .parse(getTextForParser(getConnectedJComponent().getText()));
        getConnectedJComponent().setText(formatter.format(value));
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
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null || formatter == null) {
      super.protectedSetConnecteeValue(aValue);
    } else {
      getConnectedJComponent().setText(formatter.format(aValue));
    }
  }
}
