/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import javax.swing.JPasswordField;

/**
 * JPasswordField connector.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JPasswordFieldConnector extends JTextFieldConnector {

  /**
   * Constructs a new <code>JPasswordFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param passwordField
   *            the connected JPasswordField.
   */
  public JPasswordFieldConnector(String id, JPasswordField passwordField) {
    super(id, passwordField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JPasswordField getConnectedJComponent() {
    return (JPasswordField) super.getConnectedJComponent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedJComponent().getPassword();
  }
}
