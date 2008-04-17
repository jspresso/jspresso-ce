/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A connector on a label whose role is to render an image based on its binary
 * representation taken out of the connector value.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JImageConnector extends JComponentConnector<JLabel> {

  private byte[] binaryValue;

  /**
   * Constructs a new <code>JImageConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param connectedJComponent
   *            the connected JLabel.
   */
  public JImageConnector(String id, JLabel connectedJComponent) {
    super(id, connectedJComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    // NO-OP. this is a "read-only" connector.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return binaryValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object connecteeValue) {
    this.binaryValue = (byte[]) connecteeValue;
    if (binaryValue != null) {
      getConnectedJComponent().setIcon(new ImageIcon(binaryValue));
    } else {
      getConnectedJComponent().setIcon(null);
    }
  }
}
