/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.binding.swing;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.util.image.ImageHelper;
import org.jspresso.framework.util.url.UrlHelper;

/**
 * A connector on a label whose role is to render an image based on its binary
 * representation taken out of the connector value or the image URL.
 *
 * @author Vincent Vandenschrick
 */
public class JImageConnector extends JComponentConnector<JLabel> {

  private Object  imageSource;
  private Integer scaledWidth;
  private Integer scaledHeight;

  private static final Logger LOG = LoggerFactory.getLogger(JImageConnector.class);

      /**
       * Constructs a new {@code JImageConnector} instance.
       *
       * @param id
       *     the id of the connector.
       * @param connectedJComponent
       *     the connected JLabel.
       */

  public JImageConnector(String id, JLabel connectedJComponent) {
    this(id, connectedJComponent, null, null);
  }

  /**
   * Constructs a new {@code JImageConnector} instance.
   *
   * @param id      the id of the connector.
   * @param connectedJComponent      the connected JLabel.
   * @param scaledWidth the scaled width
   * @param scaledHeight the scaled height
   */
  public JImageConnector(String id, JLabel connectedJComponent, Integer scaledWidth, Integer scaledHeight) {
    super(id, connectedJComponent);
    this.scaledWidth = scaledWidth;
    this.scaledHeight = scaledHeight;
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
    return imageSource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object connecteeValue) {
    imageSource = connecteeValue;
    byte[] scaledImage = null;
    if (imageSource != null) {
      try {
        scaledImage = ImageHelper.scaleImage(imageSource, scaledWidth, scaledHeight);
      } catch (IOException ioe) {
        LOG.warn("Failed to read image in connector {}", getId());
      }
    }
    if (scaledImage != null) {
        getConnectedJComponent().setIcon(new ImageIcon(scaledImage));
    } else {
      getConnectedJComponent().setIcon(null);
    }
  }
}
