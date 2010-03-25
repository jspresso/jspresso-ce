/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.wings;

import org.jspresso.framework.util.url.UrlHelper;
import org.wings.SByteArrayIcon;
import org.wings.SLabel;
import org.wings.SURLIcon;

/**
 * A connector on a label whose role is to render an image based on its binary
 * representation taken out of the connector value.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SImageConnector extends SComponentConnector<SLabel> {

  private Object imageSource;

  /**
   * Constructs a new <code>JImageConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param label
   *          the connected SLabel.
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
        getConnectedSComponent().setIcon(
            new SByteArrayIcon((byte[]) imageSource));
      } else {
        getConnectedSComponent().setIcon(
            new SURLIcon(UrlHelper.createURL((String) imageSource)));
      }
    } else {
      getConnectedSComponent().setIcon(null);
    }
  }
}
