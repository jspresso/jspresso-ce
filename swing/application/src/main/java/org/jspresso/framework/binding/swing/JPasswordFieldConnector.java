/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import javax.swing.JPasswordField;

/**
 * JPasswordField connector.
 * 
 * @author Vincent Vandenschrick
 */
public class JPasswordFieldConnector extends JTextFieldConnector {

  /**
   * Constructs a new {@code JPasswordFieldConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param passwordField
   *          the connected JPasswordField.
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
    char[] password = getConnectedJComponent().getPassword();
    if (password != null && password.length == 0) {
      password = null;
    }
    if (password == null) {
      return null;
    }
    return new String(password);
  }
}
