/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import javax.swing.JPasswordField;

/**
 * JPasswordField connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
    char[] password = getConnectedJComponent().getPassword();
    if (password != null && password.length == 0) {
      password = null;
    }
    return password;
  }

  /**
   * Sets the value to the connector text.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedJComponent().setText(null);
    } else {
      getConnectedJComponent().setText(new String((char[]) aValue));
    }
  }
}
