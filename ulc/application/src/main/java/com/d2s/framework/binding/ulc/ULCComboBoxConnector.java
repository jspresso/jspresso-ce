/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import com.ulcjava.base.application.ULCComboBox;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;

/**
 * JComboBox connector.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCComboBoxConnector extends ULCComponentConnector<ULCComboBox> {

  /**
   * Constructs a new <code>ULCComboBoxConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param comboBox
   *            the connected ULCExtendedComboBox.
   */
  public ULCComboBoxConnector(String id, ULCComboBox comboBox) {
    super(id, comboBox);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedULCComponent().setEnabled(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    getConnectedULCComponent().addActionListener(new IActionListener() {

      private static final long serialVersionUID = -4418440147926893407L;

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
    return getConnectedULCComponent().getSelectedItem();
  }

  /**
   * Sets the selected item in the combobox to be the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedULCComponent().setSelectedItem(aValue);
  }
}
