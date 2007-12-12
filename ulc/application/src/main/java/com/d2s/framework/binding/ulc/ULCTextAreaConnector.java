/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import com.ulcjava.base.application.ULCTextArea;

/**
 * ULCTextArea connector.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCTextAreaConnector extends
    ULCTextComponentConnector<ULCTextArea> {

  /**
   * Constructs a new <code>ULCTextAreaConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textArea
   *            the connected ULCTextArea.
   */
  public ULCTextAreaConnector(String id, ULCTextArea textArea) {
    super(id, textArea);
  }

}
