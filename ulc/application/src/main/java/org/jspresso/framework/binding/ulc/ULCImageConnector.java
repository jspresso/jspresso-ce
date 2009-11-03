/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.ulc;

import com.ulcjava.base.application.ULCLabel;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * A connector on a label whose role is to render an image based on its binary
 * representation taken out of the connector value.
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
