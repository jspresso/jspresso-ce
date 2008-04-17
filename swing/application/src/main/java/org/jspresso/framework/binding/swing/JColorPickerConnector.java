/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import java.awt.Color;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jspresso.framework.gui.swing.components.JColorPicker;
import org.jspresso.framework.util.gui.ColorHelper;


/**
 * JColorPickerConnector connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JColorPickerConnector extends JComponentConnector<JColorPicker> {

  /**
   * Constructs a new <code>JColorPickerConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param colorPicker
   *            the connected JColorPicker.
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
  protected Object getConnecteeValue() {
    Color value = getConnectedJComponent().getValue();
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
  protected void protectedUpdateState() {
    super.protectedUpdateState();
    getConnectedJComponent().setEnabled(isWritable());
  }
}
