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
package org.jspresso.framework.util.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javaxt.io.Image;
import org.apache.commons.codec.binary.Base64;

import org.jspresso.framework.util.url.UrlHelper;

/**
 * This is a simple helper class to be able to deal with images.
 *
 * @author Vincent Vandenschrick
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

  /**
   * Create image.
   *
   * @param originalImageInput the original image input
   * @return the image
   * @throws IOException the iO exception
   */
  public static Image createImage(Object originalImageInput) throws IOException {
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

  /**
   * To base 64 src.
   *
   * @param originalImageInput the original image input
   * @param format the format
   * @return the string
   * @throws IOException the iO exception
   */
  public static String toBase64Src(Object originalImageInput, String format) throws IOException {
    Image img = createImage(originalImageInput);
    return "data:image/" + format + ";base64," + Base64.encodeBase64String(img.getByteArray(format));
  }

  /**
   * From base 64 src.
   *
   * @param base64Src the base 64 src
   * @return the byte [ ]
   */
  public static byte[] fromBase64Src(String base64Src) {
    return Base64.decodeBase64(base64Src.replaceAll("^.*base64,", ""));
  }

}
