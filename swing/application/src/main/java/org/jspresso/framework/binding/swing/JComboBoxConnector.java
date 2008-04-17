/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

/**
 * JComboBox connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JComboBoxConnector extends JComponentConnector<JComboBox> {

  /**
   * Constructs a new <code>JComboBoxConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param comboBox
   *            the connected JComboBox.
   */
  public JComboBoxConnector(String id, JComboBox comboBox) {
    super(id, comboBox);
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
   * Returns the selected object in the combobox.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedJComponent().getSelectedItem();
  }

  /**
   * Sets the selected item in the combobox to be the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    getConnectedJComponent().setSelectedItem(aValue);
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
