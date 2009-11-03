/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.wings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.wings.SToggleButton;

/**
 * SToggleButton connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SToggleButtonConnector extends SComponentConnector<SToggleButton> {

  /**
   * Constructs a new <code>SToggleButtonConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param toggleButton
   *            the connected SToggleButton.
   */
  public SToggleButtonConnector(String id, SToggleButton toggleButton) {
    super(id, toggleButton);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writabilityChange() {
    super.writabilityChange();
    getConnectedSComponent().setEnabled(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {
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
   * Returns a <code>Boolean</code> object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return new Boolean(getConnectedSComponent().isSelected());
  }

  /**
   * Set the state of the button depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedSComponent().setSelected(false);
    } else {
      getConnectedSComponent().setSelected(((Boolean) aValue).booleanValue());
    }
  }

}
