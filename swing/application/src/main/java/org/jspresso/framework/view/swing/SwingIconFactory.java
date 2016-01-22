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
package org.jspresso.framework.view.swing;

import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.url.UrlHelper;
import org.jspresso.framework.view.AbstractIconFactory;

/**
 * A factory to create (and cache) swing icons.
 *
 * @author Vincent Vandenschrick
 */
public class SwingIconFactory extends AbstractIconFactory<Icon> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Icon createIcon(String urlSpec, Dimension iconSize) {
    if (urlSpec != null) {
      URL imageURL = UrlHelper.createURL(urlSpec);
      if (imageURL != null) {
        ImageIcon imageIcon = new ImageIcon(imageURL);
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(
            iconSize.getWidth(), iconSize.getHeight(), Image.SCALE_SMOOTH));
        return imageIcon;
      }
    }
    return null;
  }
}
