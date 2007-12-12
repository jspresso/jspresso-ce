/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import javax.swing.JTextArea;

/**
 * JTextArea connector.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JTextAreaConnector extends JTextComponentConnector<JTextArea> {

  /**
   * Constructs a new <code>JTextAreaConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textArea
   *            the connected JTextArea.
   */
  public JTextAreaConnector(String id, JTextArea textArea) {
    super(id, textArea);
  }

}
