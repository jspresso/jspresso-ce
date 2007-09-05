/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.text.Format;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.wings.STextField;

import com.d2s.framework.util.format.FormatAdapter;
import com.d2s.framework.util.format.IFormatter;

/**
 * SFormattedFieldConnector connector. Instances of this class must be provided
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
