/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.button.ToggleButton;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

/**
 * ToggleButton connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ToggleButtonConnector extends ComponentConnector<ToggleButton> {

  /**
   * Constructs a new <code>ToggleButtonConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param toggleButton
   *          the connected ToggleButton.
   */
  public ToggleButtonConnector(String id, ToggleButton toggleButton) {
    super(id, toggleButton);
  }

  /**
   * Returns a <code>Boolean</code> object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return new Boolean(getConnectedComponent().isSelected());
  }

  /**
   * Set the state of the button depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedComponent().setSelected(false);
    } else {
      getConnectedComponent().setSelected(((Boolean) aValue).booleanValue());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindComponent() {
    getConnectedComponent().addActionListener(new ActionListener() {

      private static final long serialVersionUID = -1330838639472290254L;

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
    getConnectedComponent().setEnabled(isWritable());
  }

}
