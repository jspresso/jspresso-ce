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

import javax.swing.JRadioButton;

/**
 * JRadioButton connector.
 *
 * @author Vincent Vandenschrick
 */
public class JRadioButtonConnector extends JToggleButtonConnector<JRadioButton> {

  private final String checkedValue;

  /**
   * Constructs a new {@code JRadioButtonConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param radioButton
   *          the connected JRadioButton.
   * @param checkedValue
   *          the value to set to the connector when the radio button is
   *          checked.
   */
  public JRadioButtonConnector(String id, JRadioButton radioButton,
      String checkedValue) {
    super(id, radioButton);
    this.checkedValue = checkedValue;
  }

  /**
   * Returns a {@code String} object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    if (getConnectedJComponent().isSelected()) {
      return checkedValue;
    }
    return null;
  }

  /**
   * Set the state of the button depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (checkedValue.equals(aValue)) {
      getConnectedJComponent().setSelected(true);
    } else {
      getConnectedJComponent().setSelected(false);
    }
  }
}
