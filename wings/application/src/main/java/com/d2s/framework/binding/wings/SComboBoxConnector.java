/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.wings.SComboBox;

/**
 * SComboBox connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SComboBoxConnector extends SComponentConnector<SComboBox> {

  /**
   * Constructs a new <code>SComboBoxConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param comboBox
   *          the connected SComboBox.
   */
  public SComboBoxConnector(String id, SComboBox comboBox) {
    super(id, comboBox);
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
   * Returns the selected object in the combobox.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedSComponent().getSelectedItem();
  }

  /**
   * Sets the selected item in the combobox to be the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedSComponent().setSelectedItem(aValue);
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
