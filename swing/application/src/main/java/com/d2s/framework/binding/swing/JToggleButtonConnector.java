/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

/**
 * JToggleButton connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JToggleButtonConnector extends JComponentConnector<JToggleButton> {

  /**
   * Constructs a new <code>JToggleButtonConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param toggleButton
   *          the connected JToggleButton.
   */
  public JToggleButtonConnector(String id, JToggleButton toggleButton) {
    super(id, toggleButton);
  }

  /**
   * Returns a <code>Boolean</code> object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return new Boolean(getConnectedJComponent().isSelected());
  }

  /**
   * Set the state of the button depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedJComponent().setSelected(false);
    } else {
      getConnectedJComponent().setSelected(((Boolean) aValue).booleanValue());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    getConnectedJComponent().addActionListener(new ActionListener() {

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
   * {@inheritDoc}
   */
  @Override
  protected void protectedUpdateState() {
    super.protectedUpdateState();
    getConnectedJComponent().setEnabled(isWritable());
  }

}
