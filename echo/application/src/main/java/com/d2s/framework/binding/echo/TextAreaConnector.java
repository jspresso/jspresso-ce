/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.TextArea;

/**
 * TextField connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TextAreaConnector extends TextComponentConnector<TextArea> {

  /**
   * Constructs a new <code>JTextfieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param textArea
   *          the connected TextArea.
   */
  public TextAreaConnector(String id, TextArea textArea) {
    super(id, textArea);
  }
}
