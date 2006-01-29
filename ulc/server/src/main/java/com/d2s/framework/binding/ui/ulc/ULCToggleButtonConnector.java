/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ui.ulc;

import com.ulcjava.base.application.ULCToggleButton;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;

/**
 * ULCToggleButton connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCToggleButtonConnector extends
    ULCComponentConnector<ULCToggleButton> {

  /**
   * Constructs a new <code>ULCToggleButtonConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param toggleButton
   *          the connected ULCToggleButton.
   */
  public ULCToggleButtonConnector(String id, ULCToggleButton toggleButton) {
    super(id, toggleButton);
  }

  /**
   * Returns a <code>Boolean</code> object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return new Boolean(getConnectedULCComponent().isSelected());
  }

  /**
   * Set the state of the button depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedULCComponent().setSelected(false);
    } else {
      getConnectedULCComponent().setSelected(((Boolean) aValue).booleanValue());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    getConnectedULCComponent().addActionListener(new IActionListener() {

      private static final long serialVersionUID = 2803794068362725099L;

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
  public void updateState() {
    super.updateState();
    getConnectedULCComponent().setEnabled(isWritable());
  }

}
