/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.awt.Color;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.d2s.framework.gui.wings.components.SColorPicker;
import com.d2s.framework.util.gui.ColorHelper;

/**
 * SColorPickerConnector connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SColorPickerConnector extends SComponentConnector<SColorPicker> {

  /**
   * Constructs a new <code>SColorPickerConnector</code> instance.
   *
   * @param id
   *          the id of the connector.
   * @param colorPicker
   *          the connected SColorPicker.
   */
  public SColorPickerConnector(String id, SColorPicker colorPicker) {
    super(id, colorPicker);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {

    getConnectedSComponent().addChangeListener(new ChangeListener() {

      /**
       * {@inheritDoc}
       */
      public void stateChanged(@SuppressWarnings("unused")
      ChangeEvent e) {
        fireConnectorValueChange();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue != null) {
      int[] rgba = ColorHelper.fromHexString((String) aValue);
      getConnectedSComponent().setValue(
          new Color(rgba[0], rgba[1], rgba[2], rgba[3]));
    } else {
      getConnectedSComponent().setValue(null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    Color value = getConnectedSComponent().getValue();
    if (value != null) {
      return ColorHelper.toHexString(value.getRed(), value.getGreen(), value
          .getBlue(), value.getAlpha());
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedSComponent().setEnabled(isWritable());
  }
}
