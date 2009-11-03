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

import org.jspresso.framework.gui.ulc.components.server.ULCColorPicker;
import org.jspresso.framework.util.gui.ColorHelper;

import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;
import com.ulcjava.base.application.util.Color;

/**
 * ULCColorPicker connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCColorPickerConnector extends
    ULCComponentConnector<ULCColorPicker> {

  /**
   * Constructs a new <code>ULCColorPickerConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param colorPicker
   *            the connected ULCColorPicker.
   */
  public ULCColorPickerConnector(String id, ULCColorPicker colorPicker) {
    super(id, colorPicker);
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
    getConnectedULCComponent().addValueChangedListener(
        new IValueChangedListener() {

          private static final long serialVersionUID = -7747055134828532559L;

          public void valueChanged(@SuppressWarnings("unused")
          ValueChangedEvent event) {
            fireConnectorValueChange();
          }
        });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    Color value = getConnectedULCComponent().getValue();
    if (value != null) {
      return ColorHelper.toHexString(value.getRed(), value.getGreen(), value
          .getBlue(), value.getAlpha());
    }
    return null;
  }

  /**
   * Set the state of the date field depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue != null) {
      int[] rgba = ColorHelper.fromHexString((String) aValue);
      getConnectedULCComponent().setValue(
          new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
    } else {
      getConnectedULCComponent().setValue(null);
    }
  }

}
