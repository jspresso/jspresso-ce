/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import org.jspresso.framework.binding.ConnectorBindingException;
import org.wings.SFormattedTextField;


/**
 * SFormattedTextFieldConnector connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SFormattedTextFieldConnector extends
    STextComponentConnector<SFormattedTextField> {

  /**
   * Constructs a new <code>SFormattedTextFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected STextField.
   */
  public SFormattedTextFieldConnector(String id, SFormattedTextField textField) {
    super(id, textField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {
    super.bindSComponent();
    getConnectedSComponent().addActionListener(new ActionListener() {

      /**
       * {@inheritDoc}
       */
      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        fireConnectorValueChange();
      }
    });
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    // don't call getValue() due to bad focusevent delivery order of
    // SFormattedTextField.
    // return getConnectedSComponent().getValue();
    try {
      if (getConnectedSComponent().getText() == null
          || getConnectedSComponent().getText().length() == 0) {
        return null;
      }
      return getConnectedSComponent().getFormatter().stringToValue(
          getConnectedSComponent().getText());
    } catch (ParseException ex) {
      throw new ConnectorBindingException(ex);
    }
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedSComponent().setValue(aValue);
  }
}
