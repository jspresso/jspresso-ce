/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.wings;

import java.text.Format;

import org.jspresso.framework.util.format.FormatAdapter;
import org.jspresso.framework.util.format.IFormatter;
import org.wings.STextField;


/**
 * Automatically adds a '%' character at the end of the pased string if needed.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   *            the id of the connector.
   * @param textField
   *            the connected STextField.
   * @param format
   *            the j2se format to use to extract the object value.
   */
  public SPercentFieldConnector(String id, STextField textField, Format format) {
    this(id, textField, new FormatAdapter(format));
  }

  /**
   * Constructs a new <code>JFormattedFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected STextField.
   * @param formatter
   *            the formatter to use to extract the object value.
   */
  public SPercentFieldConnector(String id, STextField textField,
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
