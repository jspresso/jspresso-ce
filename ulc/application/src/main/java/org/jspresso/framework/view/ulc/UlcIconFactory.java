/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.ulc;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.url.UrlHelper;
import org.jspresso.framework.view.AbstractIconFactory;

import com.ulcjava.base.application.util.ULCIcon;

/**
 * A factory to create (and cache) ULC icons.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcIconFactory extends AbstractIconFactory<ULCIcon> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected ULCIcon createIcon(String urlSpec, Dimension iconSize) {
    if (urlSpec != null) {
      URL imageURL = UrlHelper.createURL(urlSpec);
      if (imageURL != null) {
        try {
          Image iconImage = ImageIO.read(imageURL).getScaledInstance(
              iconSize.getWidth(), iconSize.getHeight(), Image.SCALE_SMOOTH);
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          BufferedImage bi = new BufferedImage(iconImage.getWidth(null),
              iconImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
          Graphics2D g2 = bi.createGraphics();
          g2.drawImage(iconImage, null, null);
          ImageIO.write(bi, "png", baos);

          return new ULCIcon(baos.toByteArray());
        } catch (Exception ignored) {
          ignored.printStackTrace();
          // The image will be empty...
        }
      }
    }
    return null;
  }

}
