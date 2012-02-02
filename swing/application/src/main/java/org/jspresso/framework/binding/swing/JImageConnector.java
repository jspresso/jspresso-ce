/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.jspresso.framework.util.url.UrlHelper;

/**
 * A connector on a label whose role is to render an image based on its binary
 * representation taken out of the connector value or the image URL.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JImageConnector extends JComponentConnector<JLabel> {

  private Object imageSource;

  /**
   * Constructs a new <code>JImageConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param connectedJComponent
   *          the connected JLabel.
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
    return imageSource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object connecteeValue) {
    this.imageSource = connecteeValue;
    if (imageSource != null) {
      if (imageSource instanceof byte[]) {
        getConnectedJComponent().setIcon(new ImageIcon((byte[]) imageSource));
      } else if (imageSource instanceof String) {
        getConnectedJComponent().setIcon(
            new ImageIcon(UrlHelper.createURL((String) imageSource)));
      }
    } else {
      getConnectedJComponent().setIcon(null);
    }
  }
}
