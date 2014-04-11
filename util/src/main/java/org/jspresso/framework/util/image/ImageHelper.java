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
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

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
   * Convenience method that returns a scaled instance of the provided
   * {@code BufferedImage}.
   *
   * @param img
   *     the original image to be scaled
   * @param targetWidth
   *     the desired width of the scaled instance, in pixels. Use null or negative if you want it scaled based on the
   *     target
   *     height,
   *     preserving ratio.
   * @param targetHeight
   *     the desired height of the scaled instance, in pixels. Use null or negative if you want it scaled based on the
   *     target width,
   *     preserving ratio.
   * @param hint
   *     one of the rendering hints that corresponds to
   *     {@code RenderingHints.KEY_INTERPOLATION} (e.g.
   *     {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
   *     {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
   *     {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
   * @param higherQuality
   *     if true, this method will use a multi-step scaling technique that
   *     provides higher quality than the usual one-step technique (only
   *     useful in downscaling cases, where {@code targetWidth} or
   *     {@code targetHeight} is smaller than the original dimensions, and
   *     generally only when the {@code BILINEAR} hint is specified)
   * @return a scaled version of the original {@code BufferedImage}
   */
  public static BufferedImage getScaledInstance(BufferedImage img, Integer targetWidth, Integer targetHeight,
                                                Object hint, boolean higherQuality) {
    int originalWidth = img.getWidth();
    int originalHeight = img.getHeight();

    if ((targetWidth != null && targetWidth > 0) || (targetHeight != null && targetHeight > 0)) {
      Integer actualTargetWidth = targetWidth;
      Integer actualTargetHeight = targetHeight;

      if (actualTargetHeight == null || actualTargetHeight <= 0) {
        actualTargetHeight = originalHeight * targetWidth / originalWidth;
      }
      if (actualTargetWidth == null || actualTargetWidth <= 0) {
        actualTargetWidth = originalWidth * targetHeight / originalHeight;
      }

      int type = BufferedImage.TYPE_INT_ARGB;
      if (img.getTransparency() == Transparency.OPAQUE) {
        type = BufferedImage.TYPE_INT_RGB;
      }
      BufferedImage ret = img;
      int w, h;
      if (higherQuality) {
        // Use multi-step technique: start with original size, then
        // scale down in multiple passes with drawImage()
        // until the target size is reached
        w = img.getWidth();
        h = img.getHeight();
      } else {
        // Use one-step technique: scale directly from original
        // size to target size with a single drawImage() call
        w = actualTargetWidth;
        h = actualTargetHeight;
      }

      do {
        if (higherQuality && w > actualTargetWidth) {
          w /= 2;
        }
        if (w < actualTargetWidth) {
          w = actualTargetWidth;
        }

        if (higherQuality && h > actualTargetHeight) {
          h /= 2;
        }
        if (h < actualTargetHeight) {
          h = actualTargetHeight;
        }

        BufferedImage tmp = new BufferedImage(w, h, type);
        Graphics2D g2 = tmp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
        g2.drawImage(ret, 0, 0, w, h, null);
        g2.dispose();

        ret = tmp;
      } while (w != actualTargetWidth || h != actualTargetHeight);

      return ret;
    }
    return img;
  }

  /**
   * Perform scaling.
   *
   * @param image
   *     the image
   * @param width
   *     the width
   * @param height
   *     the height
   * @param targetFormatName
   *     the format name
   * @return the byte [ ]
   *
   * @throws IOException
   *     the iO exception
   */
  public static byte[] performScaling(BufferedImage image, Integer width, Integer height, String targetFormatName)
      throws IOException {
    BufferedImage scaledImage = getScaledInstance(image, width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR,
        true);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(scaledImage, targetFormatName, baos);
    return baos.toByteArray();
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
    Object[] imageReaderAndInputStream = getImageReaderAndInputStream(originalImageInput);
    return scaleImageReader((ImageReader) imageReaderAndInputStream[0], (ImageInputStream) imageReaderAndInputStream[1],
        width, height, targetFormatName);
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
    Object[] imageReaderAndInputStream = getImageReaderAndInputStream(originalImageInput);
    return scaleImageReader((ImageReader) imageReaderAndInputStream[0], (ImageInputStream) imageReaderAndInputStream[1],
        width, height, null);
  }

  private static byte[] scaleImageReader(ImageReader imageReader, ImageInputStream stream, Integer width,
                                         Integer height, String targetFormatName) throws IOException {
    String actualFormatName = targetFormatName;
    if (actualFormatName == null) {
      actualFormatName  = imageReader.getFormatName();
    }
    ImageReadParam param = imageReader.getDefaultReadParam();
    imageReader.setInput(stream, true, true);
    BufferedImage bi;
    try {
      bi = imageReader.read(0, param);
    } finally {
      imageReader.dispose();
      stream.close();
    }
    return performScaling(bi, width, height, actualFormatName);
  }

  private static Object transformInput(Object originalImageInput) throws IOException {
    Object actualInput = originalImageInput;
    if (originalImageInput instanceof String) {
      actualInput = UrlHelper.createURL((String) originalImageInput).openStream();
    } else if (originalImageInput instanceof URL) {
      actualInput = ((URL) originalImageInput).openStream();
    } else if (originalImageInput instanceof byte[]) {
      actualInput = new ByteArrayInputStream((byte[]) originalImageInput);
    }
    return actualInput;
  }

  private static Object[] getImageReaderAndInputStream(Object originalImageInput) throws IOException {
    ImageInputStream iis = ImageIO.createImageInputStream(transformInput(originalImageInput));
    ImageReader ir = ImageIO.getImageReaders(iis).next();
    return new Object[]{ir, iis};
  }
}
