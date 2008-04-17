/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.wings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.wings.SToggleButton;

/**
 * SToggleButton connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SToggleButtonConnector extends SComponentConnector<SToggleButton> {

  /**
   * Constructs a new <code>SToggleButtonConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param toggleButton
   *            the connected SToggleButton.
   */
  public SToggleButtonConnector(String id, SToggleButton toggleButton) {
    super(id, toggleButton);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedSComponent().setEnabled(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {
    getConnectedSComponent().addActionListener(new ActionListener() {

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
   * Returns a <code>Boolean</code> object mapping the state of the button.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return new Boolean(getConnectedSComponent().isSelected());
  }

  /**
   * Set the state of the button depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedSComponent().setSelected(false);
    } else {
      getConnectedSComponent().setSelected(((Boolean) aValue).booleanValue());
    }
  }

}
