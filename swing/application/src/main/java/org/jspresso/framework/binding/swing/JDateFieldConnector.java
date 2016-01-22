/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.swing;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jspresso.framework.binding.ConnectorBindingException;
import org.jspresso.framework.gui.swing.components.JDateField;

/**
 * JDateField connector.
 *
 * @author Vincent Vandenschrick
 */
public class JDateFieldConnector extends JComponentConnector<JDateField> {

  private Color savedSelectedTextColor;

  /**
   * Constructs a new {@code JDateField} instance.
   *
   * @param id
   *          the connector identifier.
   * @param dateField
   *          the connected JDateField.
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

      @Override
      public void stateChanged(ChangeEvent e) {
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
    // don't call getValue() due to bad focus event delivery order of
    // JFormattedTextField.
    // return getConnectedJComponent().getValue();
    try {
      if (getConnectedJComponent().getFormattedTextField().getText() == null
          || getConnectedJComponent().getFormattedTextField().getText()
              .length() == 0) {
        return null;
      }
      return getConnectedJComponent()
          .getFormattedTextField()
          .getFormatter()
          .stringToValue(
              getConnectedJComponent().getFormattedTextField().getText());
    } catch (ParseException ex) {
      throw new ConnectorBindingException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedReadabilityChange() {
    super.protectedReadabilityChange();
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
  protected void protectedWritabilityChange() {
    getConnectedJComponent().setEditable(isWritable());
  }
}
