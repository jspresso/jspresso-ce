/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

/**
 * JTextField connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JTextFieldConnector extends JTextComponentConnector<JTextField> {

  /**
   * Constructs a new <code>JTextfieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected JTextField.
   */
  public JTextFieldConnector(String id, JTextField textField) {
    super(id, textField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    super.bindJComponent();
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
}
