/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.text.Format;

import org.wings.STextField;

import com.d2s.framework.util.format.FormatAdapter;
import com.d2s.framework.util.format.IFormatter;

/**
 * Automatically adds a '%' character at the end of the pased string if needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SPercentFieldConnector extends SFormattedFieldConnector {

  private static final String PERCENT = "%";

  /**
   * Constructs a new <code>JFormattedFieldConnector</code> instance.
   *
   * @param id
   *          the id of the connector.
   * @param textField
   *          the connected STextField.
   * @param formatter
   *          the formatter to use to extract the object value.
   */
  public SPercentFieldConnector(String id, STextField textField,
      IFormatter formatter) {
    super(id, textField, formatter);
  }

  /**
   * Constructs a new <code>JFormattedFieldConnector</code> instance.
   *
   * @param id
   *          the id of the connector.
   * @param textField
   *          the connected STextField.
   * @param format
   *          the j2se format to use to extract the object value.
   */
  public SPercentFieldConnector(String id, STextField textField, Format format) {
    this(id, textField, new FormatAdapter(format));
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
