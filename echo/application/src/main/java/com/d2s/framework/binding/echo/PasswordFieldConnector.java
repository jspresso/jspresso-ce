/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.PasswordField;

/**
 * PasswordField connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class PasswordFieldConnector extends TextFieldConnector {

  /**
   * Constructs a new <code>PasswordFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param passwordField
   *          the connected PasswordField.
   */
  public PasswordFieldConnector(String id, PasswordField passwordField) {
    super(id, passwordField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PasswordField getConnectedComponent() {
    return (PasswordField) super.getConnectedComponent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    String password = (String) super.getConnecteeValue();
    if (password != null) {
      return password.toCharArray();
    }
    return null;
  }
}
