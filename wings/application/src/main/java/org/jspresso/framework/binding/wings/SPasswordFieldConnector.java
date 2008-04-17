/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.wings;

import org.wings.SPasswordField;

/**
 * SPasswordField connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SPasswordFieldConnector extends STextFieldConnector {

  /**
   * Constructs a new <code>SPasswordFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param passwordField
   *            the connected SPasswordField.
   */
  public SPasswordFieldConnector(String id, SPasswordField passwordField) {
    super(id, passwordField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected SPasswordField getConnectedSComponent() {
    return (SPasswordField) super.getConnectedSComponent();
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
