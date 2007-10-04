/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import com.ulcjava.base.application.ULCLabel;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * A connector on a label whose role is to render an image based on its binary
 * representation taken out of the connector value.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCImageConnector extends ULCComponentConnector<ULCLabel> {

  private byte[] binaryValue;

  /**
   * Constructs a new <code>ULCImageConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param connectedULCComponent
   *            the connected ULCLabel.
   */
  public ULCImageConnector(String id, ULCLabel connectedULCComponent) {
    super(id, connectedULCComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
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
      getConnectedULCComponent().setIcon(new ULCIcon(binaryValue));
    } else {
      getConnectedULCComponent().setIcon(null);
    }
  }
}
