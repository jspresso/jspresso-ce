/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.TextField;

/**
 * TextField connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TextFieldConnector extends TextComponentConnector<TextField> {

  /**
   * Constructs a new <code>JTextfieldConnector</code> instance.
   *
   * @param id
   *          the id of the connector.
   * @param textField
   *          the connected TextField.
   */
  public TextFieldConnector(String id, TextField textField) {
    super(id, textField);
  }
}
