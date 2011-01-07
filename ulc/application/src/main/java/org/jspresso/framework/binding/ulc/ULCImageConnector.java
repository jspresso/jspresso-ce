/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.util.url.UrlHelper;

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

  private Object imageSource;

  /**
   * Constructs a new <code>ULCImageConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param connectedULCComponent
   *          the connected ULCLabel.
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
    return imageSource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object connecteeValue) {
    this.imageSource = connecteeValue;
    if (imageSource != null) {
      if (imageSource instanceof byte[]) {
        getConnectedULCComponent().setIcon(new ULCIcon((byte[]) imageSource));
      } else if (imageSource instanceof String) {
        getConnectedULCComponent().setIcon(
            new ULCIcon(UrlHelper.createURL((String) imageSource)));
      }
    } else {
      getConnectedULCComponent().setIcon(null);
    }
  }
}
