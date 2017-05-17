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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

  /**
   * The constant SVG is &quot;svg&quot;.
   */
  public static final String SVG = "svg";

  /**
   * The constant SVG_CONTENT_TYPE is &quot;image/svg+xml&quot;.
   */
  public static final String SVG_CONTENT_TYPE = "image/svg+xml";

  /**
   * The constant PNG is &quot;png&quot;.
   */
  public static final String PNG = "png";

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
    if (SVG.equals(targetFormatName)) {
      String svgImage = createSvgImage(originalImageInput);
      if (width != null) {
        if (svgImage.matches("(?s).*width\\s*=\\s*\"\\d*\".*")) {
          svgImage = svgImage.replaceFirst("width\\s*=\\s*\"\\d*\"", "width=\"" + width + "\"");
        } else {
          svgImage = svgImage.replaceFirst("<svg", "<svg width=\"" + width + "\"");
        }
      }
      if (height != null) {
        if (svgImage.matches("(?s).*height\\s*=\\s*\"\\d*\".*")) {
          svgImage = svgImage.replaceFirst("height\\s*=\\s*\"\\d*\"", "height=\"" + height + "\"");
        } else {
          svgImage = svgImage.replaceFirst("<svg", "<svg height=\"" + height + "\"");
        }
      }
      return svgImage.getBytes(StandardCharsets.UTF_8);
    } else {
      byte[] imageBytes = extractImageBytes(originalImageInput);
      Image image;
      try {
        image = new Image(imageBytes);
      } catch (Throwable ex) {
        if (originalImageInput instanceof byte[]) {
          LOG.warn("Received an invalid image content. Cannot transform so returning original content.", ex);
          return (byte[]) originalImageInput;
        }
        LOG.warn("Received an invalid image content. Cannot transform so returning null.", ex);
        return null;
      }
      try {
        image.rotate();
      } catch (Throwable ex) {
        LOG.warn("Cannot rotate the image so leaving it as is.", ex);
      }
      try {
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
      } catch (Throwable ex) {
        LOG.warn("Cannot resize the image so leaving it as is.", ex);
      }
      if (targetFormatName != null) {
        return image.getByteArray(targetFormatName);
      }
      return image.getByteArray("PNG");
    }
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
   * Create SVG image.
   *
   * @param originalImageInput
   *     the original image input
   * @return the SVG image
   *
   * @throws IOException
   *     the iO exception
   */
  public static String createSvgImage(Object originalImageInput) throws IOException {
    InputStream svgInputStream;
    if (originalImageInput instanceof byte[]) {
      svgInputStream = new ByteArrayInputStream((byte[]) originalImageInput);
    } else if (originalImageInput instanceof String) {
      try {
        svgInputStream = UrlHelper.createURL((String) originalImageInput).openStream();
      } catch (IOException ioe) {
        svgInputStream = new ByteArrayInputStream(((String) originalImageInput).getBytes(StandardCharsets.UTF_8));
      }
    } else if (originalImageInput instanceof URL) {
      svgInputStream = ((URL) originalImageInput).openStream();
    } else if (originalImageInput instanceof InputStream) {
      svgInputStream = (InputStream) originalImageInput;
    } else {
      throw new RuntimeException("Unsupported SVG image input.");
    }
    ByteArrayOutputStream svgOutputStream = new ByteArrayOutputStream();
    try {
      IoHelper.copyStream(svgInputStream, svgOutputStream);
      return svgOutputStream.toString(StandardCharsets.UTF_8.name());
    } finally {
      if (svgInputStream != null) {
        svgInputStream.close();
      }
      svgOutputStream.close();
    }
  }

  /**
   * To base 64 src.
   *
   * @param originalImageInput
   *     the original image input
   * @param format
   *     the format
   * @return the string
   *
   * @throws IOException
   *     the iO exception
   */
  public static String toBase64Src(Object originalImageInput, String format) throws IOException {
    Image img = new Image(extractImageBytes(originalImageInput));
    return "data:image/" + format + ";base64," + Base64.encodeBase64String(img.getByteArray(format));
  }

  /**
   * From base 64 src.
   *
   * @param base64Src
   *     the base 64 src
   * @return the byte [ ]
   */
  public static byte[] fromBase64Src(String base64Src) {
    return Base64.decodeBase64(base64Src.replaceAll("^.*base64,", ""));
  }


  /**
   * Load image from project's resource path.
   *
   * @param resourcePath
   *     The path to the resource.
   * @return the image as bytes.
   *
   * @throws IOException
   *     If resource cannot be read.
   */
  public static byte[] loadImage(String resourcePath) throws IOException {
    if (!resourcePath.startsWith("/")) {
      resourcePath = "/" + resourcePath;
    }
    try (InputStream is = ImageHelper.class.getResourceAsStream(resourcePath)) {
      return extractImageBytes(is);
    }
  }

}
