/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.wings.SCheckBox;

/**
 * SCheckBox connector.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SCheckBoxConnector extends SComponentConnector<SCheckBox> {

  /**
   * Constructs a new <code>SCheckBoxConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param checkBox
   *            the connected SCheckBox.
   */
  public SCheckBoxConnector(String id, SCheckBox checkBox) {
    super(id, checkBox);
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
