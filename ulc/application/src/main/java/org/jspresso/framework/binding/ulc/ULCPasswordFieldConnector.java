/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.ulc;

import com.ulcjava.base.application.ULCPasswordField;

/**
 * ULCPasswordField connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCPasswordFieldConnector extends ULCTextFieldConnector {

  /**
   * Constructs a new <code>ULCPasswordFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param passwordField
   *            the connected ULCPasswordField.
   */
  public ULCPasswordFieldConnector(String id, ULCPasswordField passwordField) {
    super(id, passwordField);
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
