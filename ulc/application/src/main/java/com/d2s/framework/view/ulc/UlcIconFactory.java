/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.d2s.framework.util.url.UrlHelper;
import com.d2s.framework.view.AbstractIconFactory;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * A factory to create (and cache) ULC icons.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
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
              iconSize.width, iconSize.height, Image.SCALE_SMOOTH);
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
