/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import org.wings.STextArea;

/**
 * STextArea connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class STextAreaConnector extends STextComponentConnector<STextArea> {

  /**
   * Constructs a new <code>STextAreaConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textArea
   *            the connected STextArea.
   */
  public STextAreaConnector(String id, STextArea textArea) {
    super(id, textArea);
  }

}
