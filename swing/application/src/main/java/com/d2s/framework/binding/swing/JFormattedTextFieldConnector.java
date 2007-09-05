/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;

import com.d2s.framework.binding.ConnectorBindingException;

/**
 * JFormattedTextFieldConnector connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JFormattedTextFieldConnector extends
    JTextComponentConnector<JFormattedTextField> {

  /**
   * Constructs a new <code>JFormattedTextFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected JTextField.
   */
  public JFormattedTextFieldConnector(String id, JFormattedTextField textField) {
    super(id, textField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    super.bindJComponent();
    getConnectedJComponent().addActionListener(new ActionListener() {

      /**
       * {@inheritDoc}
       */
      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        fireConnectorValueChange();
      }
    });
    getConnectedJComponent().addPropertyChangeListener("value",
        new PropertyChangeListener() {

          public void propertyChange(@SuppressWarnings("unused")
          PropertyChangeEvent evt) {
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
    // JFormattedTextField.
    // return getConnectedJComponent().getValue();
    try {
      if (getConnectedJComponent().getText() == null
          || getConnectedJComponent().getText().length() == 0) {
        return null;
      }
      return getConnectedJComponent().getFormatter().stringToValue(
          getConnectedJComponent().getText());
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
  protected void protectedSetConnecteeValue(Object aValue) {
    getConnectedJComponent().setValue(aValue);
  }
}
