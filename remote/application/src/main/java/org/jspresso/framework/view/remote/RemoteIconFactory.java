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
package org.jspresso.framework.view.remote;

import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.AbstractIconFactory;

/**
 * A factory to create (and cache) remote icons.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteIconFactory extends AbstractIconFactory<RIcon> {

  private IGUIDGenerator<String> guidGenerator;

  /**
   * Sets the guidGenerator.
   *
   * @param guidGenerator
   *          the guidGenerator to set.
   */
  public void setGuidGenerator(IGUIDGenerator<String> guidGenerator) {
    this.guidGenerator = guidGenerator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RIcon createIcon(String urlSpec, Dimension iconSize) {
    if (urlSpec != null) {
      RIcon imageIcon = new RIcon(guidGenerator.generateGUID());
      imageIcon.setImageUrlSpec(ResourceProviderServlet
          .computeImageResourceDownloadUrl(urlSpec, iconSize));
      imageIcon.setDimension(iconSize);
      return imageIcon;
    }
    return null;
  }
}
