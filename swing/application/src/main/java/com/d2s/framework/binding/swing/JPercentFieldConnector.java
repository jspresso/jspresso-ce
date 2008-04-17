/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.text.Format;

import javax.swing.JTextField;

import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;


/**
 * Automatically adds a '%' character at the end of the pased string if needed.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JPercentFieldConnector extends JFormattedFieldConnector {

  private static final String PERCENT = "%";

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
  public JPercentFieldConnector(String id, JTextField textField, Format format) {
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
  public JPercentFieldConnector(String id, JTextField textField,
      IFormatter formatter) {
    super(id, textField, formatter);
  }

  /**
   * Automatically adds a '%' character at the end of the pased string if
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
