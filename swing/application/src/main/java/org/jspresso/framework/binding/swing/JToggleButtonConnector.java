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

import javax.swing.JToggleButton;

/**
 * JToggleButton connector.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual toggle button class.
 */
public class JToggleButtonConnector<E extends JToggleButton> extends
    JComponentConnector<E> {

  /**
   * Constructs a new {@code JToggleButtonConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param toggleButton
   *          the connected JToggleButton.
   */
  public JToggleButtonConnector(String id, E toggleButton) {
    super(id, toggleButton);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    getConnectedJComponent().addActionListener(new ActionListener() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        fireConnectorValueChange();
      }
    });
  }

  /**
   * Returns a {@code Boolean} object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedJComponent().isSelected();
  }

  /**
   * Set the state of the button depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedJComponent().setSelected(false);
    } else {
      getConnectedJComponent().setSelected((Boolean) aValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedWritabilityChange() {
    super.protectedWritabilityChange();
    getConnectedJComponent().setEnabled(isWritable());
  }

}
