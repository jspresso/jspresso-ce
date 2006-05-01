/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import java.util.Collections;
import java.util.List;

import com.ulcjava.base.application.ULCComboBox;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;

/**
 * JComboBox connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCComboBoxConnector extends ULCComponentConnector<ULCComboBox> {

  private List<String> genuineValues;

  /**
   * Constructs a new <code>ULCComboBoxConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param comboBox
   *          the connected ULCComboBox.
   * @param genuineValues
   *          the values which are used when getting the connector value. I18N
   *          in ULC can't be handled in renderers. That is why this workaround
   *          has to be used.
   */
  public ULCComboBoxConnector(String id, ULCComboBox comboBox,
      List<String> genuineValues) {
    super(id, comboBox);
    this.genuineValues = genuineValues;
  }

  /**
   * Returns the selected object in the combobox.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return genuineValues.get(getConnectedULCComponent().getSelectedIndex());
  }

  /**
   * Sets the selected item in the combobox to be the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedULCComponent().setSelectedIndex(
        Collections.binarySearch(genuineValues, (String) aValue));
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
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedULCComponent().setEnabled(isWritable());
  }
}
