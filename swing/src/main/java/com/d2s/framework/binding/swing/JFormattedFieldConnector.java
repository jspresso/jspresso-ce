/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.text.Format;
import java.text.ParseException;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.d2s.framework.binding.ConnectorBindingException;
import com.d2s.framework.util.format.FormatAdapter;
import com.d2s.framework.util.format.IFormatter;

/**
 * JFormattedFieldConnector connector. Instances of this class must be provided
 * with a <code>Format</code> using the constructor. If not set, the string
 * representation contained in the text component will be used "as is" as the
 * connector value.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
   *          the id of the connector.
   * @param textField
   *          the connected JTextField.
   * @param formatter
   *          the formatter to use to extract the object value.
   */
  public JFormattedFieldConnector(String id, JTextField textField,
      IFormatter formatter) {
    super(id, textField);
    this.formatter = formatter;
  }

  /**
   * Constructs a new <code>JFormattedFieldConnector</code> instance.
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
        return formatter.parse(getConnectedJComponent().getText());
      } catch (ParseException ex) {
        throw new ConnectorBindingException(ex);
      }
    }
    return super.getConnecteeValue();
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
