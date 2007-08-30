/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import com.d2s.framework.gui.ulc.components.server.ULCColorPicker;
import com.d2s.framework.util.gui.ColorHelper;
import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;
import com.ulcjava.base.application.util.Color;

/**
 * ULCColorPicker connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
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
   *          the id of the connector.
   * @param colorPicker
   *          the connected ULCColorPicker.
   */
  public ULCColorPickerConnector(String id, ULCColorPicker colorPicker) {
    super(id, colorPicker);
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
  public void updateState() {
    super.updateState();
    getConnectedULCComponent().setEnabled(isWritable());
  }

}
