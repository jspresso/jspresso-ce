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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jspresso.framework.gui.swing.components.JColorPicker;
import org.jspresso.framework.util.gui.ColorHelper;

/**
 * JColorPickerConnector connector.
 *
 * @author Vincent Vandenschrick
 */
public class JColorPickerConnector extends JComponentConnector<JColorPicker> {

  /**
   * Constructs a new {@code JColorPickerConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param colorPicker
   *          the connected JColorPicker.
   */
  public JColorPickerConnector(String id, JColorPicker colorPicker) {
    super(id, colorPicker);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {

    getConnectedJComponent().addChangeListener(new ChangeListener() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void stateChanged(ChangeEvent e) {
        fireConnectorValueChange();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    Color value = getConnectedJComponent().getValue();
    if (value != null) {
      return ColorHelper.toHexString(value.getRed(), value.getGreen(),
          value.getBlue(), value.getAlpha());
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue != null) {
      int[] rgba = ColorHelper.fromHexString((String) aValue);
      getConnectedJComponent().setValue(
          new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
    } else {
      getConnectedJComponent().setValue(null);
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
