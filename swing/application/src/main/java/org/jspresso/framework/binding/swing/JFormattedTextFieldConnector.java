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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;

import org.jspresso.framework.binding.ConnectorBindingException;

/**
 * JFormattedTextFieldConnector connector.
 *
 * @author Vincent Vandenschrick
 */
public class JFormattedTextFieldConnector extends
    JTextComponentConnector<JFormattedTextField> {

  /**
   * Constructs a new {@code JFormattedTextFieldConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param textField
   *          the connected JTextField.
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
      @Override
      public void actionPerformed(ActionEvent e) {
        fireConnectorValueChange();
      }
    });
    getConnectedJComponent().addPropertyChangeListener("value",
        new PropertyChangeListener() {

          @Override
          public void propertyChange(PropertyChangeEvent evt) {
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
