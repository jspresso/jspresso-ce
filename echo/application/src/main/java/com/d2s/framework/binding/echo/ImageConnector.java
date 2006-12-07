/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.AwtImageReference;
import echopointng.ImageIcon;

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
public class ImageConnector extends ComponentConnector<ImageIcon> {

  private byte[] binaryValue;

  /**
   * Constructs a new <code>ImageConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param connectedComponent
   *          the connected ImageIcon.
   */
  public ImageConnector(String id, ImageIcon connectedComponent) {
    super(id, connectedComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindComponent() {
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
      getConnectedComponent().setIcon(
          new AwtImageReference(new javax.swing.ImageIcon(binaryValue)
              .getImage()));
    } else {
      getConnectedComponent().setIcon(null);
    }
  }
}
