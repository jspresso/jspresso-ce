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
package org.jspresso.framework.binding.ulc;

import com.ulcjava.base.application.ULCComboBox;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;

/**
 * JComboBox connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCComboBoxConnector extends ULCComponentConnector<ULCComboBox> {

  /**
   * Constructs a new <code>ULCComboBoxConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param comboBox
   *            the connected ULCExtendedComboBox.
   */
  public ULCComboBoxConnector(String id, ULCComboBox comboBox) {
    super(id, comboBox);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writabilityChange() {
    super.writabilityChange();
    getConnectedULCComponent().setEnabled(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    getConnectedULCComponent().addActionListener(new IActionListener() {

      private static final long serialVersionUID = -4418440147926893407L;

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
   * Returns the selected object in the combobox.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedULCComponent().getSelectedItem();
  }

  /**
   * Sets the selected item in the combobox to be the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedULCComponent().setSelectedItem(aValue);
  }
}
