/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jspresso.framework.binding.ConnectorBindingException;

import com.d2s.framework.gui.swing.components.JDateField;

/**
 * JDateField connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JDateFieldConnector extends JComponentConnector<JDateField> {

  private Color savedSelectedTextColor;

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param id
   *            the connector identifier.
   * @param dateField
   *            the connected JDateField.
   */
  public JDateFieldConnector(String id, JDateField dateField) {
    super(id, dateField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    getConnectedJComponent().addChangeListener(new ChangeListener() {

      public void stateChanged(@SuppressWarnings("unused")
      ChangeEvent e) {
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
      if (getConnectedJComponent().getFormattedTextField().getText() == null
          || getConnectedJComponent().getFormattedTextField().getText()
              .length() == 0) {
        return null;
      }
      return getConnectedJComponent().getFormattedTextField().getFormatter()
          .stringToValue(
              getConnectedJComponent().getFormattedTextField().getText());
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedUpdateState() {
    super.protectedUpdateState();
    if (isReadable()) {
      if (savedSelectedTextColor != null) {
        getConnectedJComponent().getFormattedTextField().setSelectedTextColor(
            savedSelectedTextColor);
      }
      savedSelectedTextColor = null;
    } else if (savedSelectedTextColor == null) {
      savedSelectedTextColor = getConnectedJComponent().getFormattedTextField()
          .getSelectedTextColor();
      getConnectedJComponent().getFormattedTextField().setSelectedTextColor(
          getConnectedJComponent().getFormattedTextField().getSelectionColor());
    }
    getConnectedJComponent().setEditable(isWritable());
  }
}
