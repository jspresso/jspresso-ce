/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import org.wings.SByteArrayIcon;
import org.wings.SLabel;

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
public class SImageConnector extends SComponentConnector<SLabel> {

  private byte[] binaryValue;

  /**
   * Constructs a new <code>JImageConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param label
   *            the connected SLabel.
   */
  public SImageConnector(String id, SLabel label) {
    super(id, label);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {
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
  protected void setConnecteeValue(Object connecteeValue) {
    this.binaryValue = (byte[]) connecteeValue;
    if (binaryValue != null) {
      getConnectedSComponent().setIcon(new SByteArrayIcon(binaryValue));
    } else {
      getConnectedSComponent().setIcon(null);
    }
  }
}
