/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import javaxt.io.Image;

import org.jspresso.framework.util.url.UrlHelper;

/**
 * This is a simple helper class to be able to deal with images.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public final class ImageHelper {

  private ImageHelper() {
    // private constructor for helper class.
  }

  /**
   * Scale image.
   *
   * @param originalImageInput
   *     the original image input.
   * @param width
   *     the width
   * @param height
   *     the height
   * @param targetFormatName
   *     the format name.
   * @return the buffered input stream
   *
   * @throws IOException
   *     the iO exception
   */
  public static byte[] scaleImage(Object originalImageInput, Integer width, Integer height, String targetFormatName)
      throws IOException {
    Image image = createImage(originalImageInput);
    image.rotate();
    if (width != null && height != null) {
      image.resize(width, height, true);
    } else if (width != null) {
      image.setWidth(width);
    } else if (height != null) {
      image.setHeight(height);
    }
    if (targetFormatName != null) {
      return image.getByteArray(targetFormatName);
    }
    return image.getByteArray();
  }

  /**
   * Scale image.
   *
   * @param originalImageInput
   *     the original image input.
   * @param width
   *     the width
   * @param height
   *     the height
   * @return the buffered input stream
   *
   * @throws IOException
   *     the iO exception
   */
  public static byte[] scaleImage(Object originalImageInput, Integer width, Integer height) throws IOException {
    return scaleImage(originalImageInput, width, height, null);
  }

  private static Image createImage(Object originalImageInput) throws IOException {
    Image image;
    if (originalImageInput instanceof byte[]) {
      image = new Image((byte[]) originalImageInput);
    } else if (originalImageInput instanceof String) {
      image = new Image(UrlHelper.createURL((String) originalImageInput).openStream());
    } else if (originalImageInput instanceof URL) {
      image = new Image(((URL) originalImageInput).openStream());
    } else if (originalImageInput instanceof InputStream) {
      image = new Image((InputStream) originalImageInput);
    } else {
      throw new RuntimeException("Unsupported image input.");
    }
    return image;
  }

}
