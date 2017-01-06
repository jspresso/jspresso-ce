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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javaxt.io.Image;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.url.UrlHelper;

/**
 * This is a simple helper class to be able to deal with images.
 *
 * @author Vincent Vandenschrick
 */
public final class ImageHelper {

  private final static Logger LOG = LoggerFactory.getLogger(ImageHelper.class);

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
    byte[] imageBytes = extractImageBytes(originalImageInput);
    Image image = new Image(imageBytes);
    if (image == null) {
      if (originalImageInput instanceof byte[]) {
        LOG.warn("Received an invalid image content. Cannot transform so returning original content.");
        return (byte[]) originalImageInput;
      }
      LOG.warn("Received an invalid image content. Cannot transform so returning null.");
      return null;
    }
    image.rotate();
    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();
    if (width != null && height != null && width != originalWidth && height != originalHeight) {
      image.resize(width, height, true);
    } else if (height == null && width != null && width != originalWidth) {
      image.setWidth(width);
    } else if (width == null && height != null && height != originalHeight) {
      image.setHeight(height);
    } else {
      return imageBytes;
    }
    if (targetFormatName != null) {
      return image.getByteArray(targetFormatName);
    }
    return image.getByteArray("PNG");
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
   * @param originalImageInput
   *     the original image input
   * @return the image
   *
   * @throws IOException
   *     the iO exception
   */
  public static byte[] extractImageBytes(Object originalImageInput) throws IOException {
    byte[] imageBytes;
    if (originalImageInput instanceof byte[]) {
      imageBytes = (byte[]) originalImageInput;
    } else {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      if (originalImageInput instanceof String) {
        IoHelper.copyStream(UrlHelper.createURL((String) originalImageInput).openStream(), baos);
      } else if (originalImageInput instanceof URL) {
        IoHelper.copyStream(((URL) originalImageInput).openStream(), baos);
      } else if (originalImageInput instanceof InputStream) {
        IoHelper.copyStream((InputStream) originalImageInput, baos);
      } else {
        throw new RuntimeException("Unsupported image input.");
      }
      imageBytes = baos.toByteArray();
    }
    return imageBytes;
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
    Image img = new Image(extractImageBytes(originalImageInput));
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


  /**
   * Load image from project's resource path.
   * @param resourcePath The path to the resource.
   * @return the image as bytes.
   * @throws IOException If resource cannot be read.
   */
  public static byte[] loadImage(String resourcePath) throws IOException {
    if (!resourcePath.startsWith("/")) {
      resourcePath = "/" + resourcePath;
    }
    try (InputStream is =ImageHelper.class.getResourceAsStream(resourcePath)){
      return extractImageBytes(is);
    }
  }

}
