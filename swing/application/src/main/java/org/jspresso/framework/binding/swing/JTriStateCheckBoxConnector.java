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

import chrriis.dj.swingsuite.JTriStateCheckBox;
import chrriis.dj.swingsuite.JTriStateCheckBox.CheckState;

/**
 * JTriStateCheckbox connector.
 *
 * @author Vincent Vandenschrick
 */
public class JTriStateCheckBoxConnector extends
    JToggleButtonConnector<JTriStateCheckBox> {

  /**
   * Constructs a new {@code JTriStateCheckBoxConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param triStateCheckBox
   *          the connected JTriStateCheckBox.
   */
  public JTriStateCheckBoxConnector(String id,
      JTriStateCheckBox triStateCheckBox) {
    super(id, triStateCheckBox);
  }

  /**
   * Returns a {@code Boolean} object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    if (getConnectedJComponent().getState() == CheckState.INDETERMINATE) {
      return null;
    }
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
      getConnectedJComponent().setState(CheckState.INDETERMINATE);
    } else {
      getConnectedJComponent().setSelected((Boolean) aValue);
    }
  }
}
