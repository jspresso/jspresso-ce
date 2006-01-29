/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.text.JTextComponent;

/**
 * This abstract class serves as the base class for all JTextComponent
 * connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          The actual class of the subclass of <code>JTextComponent</code>.
 */
public abstract class JTextComponentConnector<E extends JTextComponent> extends
    JComponentConnector<E> {

  private Color savedSelectedTextColor;

  /**
   * Constructs a new <code>JTextComponentConnector</code> instance. The
   * connector will listen to <code>focusLost</code> events so subclass only
   * need to listen to other unhandled events if necessary.
   * 
   * @param id
   *          the connector identifier.
   * @param textComponent
   *          the connected JTextComponent.
   */
  public JTextComponentConnector(String id, E textComponent) {
    super(id, textComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    getConnectedJComponent().addFocusListener(new FocusAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void focusLost(FocusEvent e) {
        if (!e.isTemporary()) {
          fireConnectorValueChange();
        }
      }
    });
  }

  /**
   * Gets the value out of the connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedJComponent().getText();
  }

  /**
   * Sets the value to the connector text.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedJComponent().setText(null);
    } else {
      getConnectedJComponent().setText(aValue.toString());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedUpdateState() {
    super.protectedUpdateState();
    if (isReadable()) {
      if (savedSelectedTextColor != null) {
        getConnectedJComponent().setSelectedTextColor(savedSelectedTextColor);
      }
      savedSelectedTextColor = null;
    } else if (savedSelectedTextColor == null) {
      savedSelectedTextColor = getConnectedJComponent().getSelectedTextColor();
      getConnectedJComponent().setSelectedTextColor(
          getConnectedJComponent().getSelectionColor());
    }
    getConnectedJComponent().setEditable(isWritable());
  }
}
