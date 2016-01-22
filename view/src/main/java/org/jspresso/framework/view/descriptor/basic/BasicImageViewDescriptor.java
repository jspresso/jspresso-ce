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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.util.image.IScalableImageAware;
import org.jspresso.framework.view.descriptor.IImageViewDescriptor;

/**
 * This type of view descriptor is used to display a binary property or a string
 * property containing an URL as an image. By default, binary properties are
 * rendered as button fields that allow to upload, download and query size of
 * the binary content. This button field visually indicate whether the binary
 * property is empty or not. Whenever you know that the underlying property is
 * used to store image content, you can explicitly define an image view backed
 * by the binary property descriptor and use it in your UI. Jspresso will then
 * display the image whose content is stored in the binary property directly in
 * the UI.
 *
 * @author Vincent Vandenschrick
 */
public class BasicImageViewDescriptor extends BasicPropertyViewDescriptor
    implements IImageViewDescriptor {

  private boolean scrollable;
  private boolean drawable;
  private Integer scaledWidth;
  private Integer scaledHeight;

  /**
   * Constructs a new {@code BasicImageViewDescriptor} instance.
   */
  protected BasicImageViewDescriptor() {
    scrollable = true;
  }

  /**
   * Gets the scrollable.
   *
   * @return the scrollable.
   */
  @Override
  public boolean isScrollable() {
    return scrollable;
  }

  /**
   * Returns {@code true} if scrollable.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isHorizontallyScrollable() {
    return isScrollable();
  }

  /**
   * Returns {@code true} if scrollable.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isVerticallyScrollable() {
    return isScrollable();
  }

  /**
   * Configures the image view to be either cropped or scrollable when the
   * display area is too small to display it. A value of {@code true}
   * (default) means that the image view will be made scrollable.
   *
   * @param scrollable
   *          the scrollable to set.
   */
  public void setScrollable(boolean scrollable) {
    this.scrollable = scrollable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getScaledWidth() {
    if (scaledWidth == null && getModelDescriptor() instanceof IScalableImageAware) {
      return ((IScalableImageAware) getModelDescriptor()).getScaledWidth();
    }
    return scaledWidth;
  }

  /**
   * Sets scaled width. This property, when set to a positive integer will force the image width to be resized to the
   * target value. If only one of the 2 scaled dimensions is set, then the image is scaled by preserving its aspect
   * ratio.
   *
   * @param scaledWidth
   *     the scaled width
   */
  public void setScaledWidth(Integer scaledWidth) {
    this.scaledWidth = scaledWidth;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getScaledHeight() {
    if (scaledHeight == null && getModelDescriptor() instanceof IScalableImageAware) {
      return ((IScalableImageAware) getModelDescriptor()).getScaledHeight();
    }
    return scaledHeight;
  }

  /**
   * Sets scaled height. This property, when set to a positive integer will force the image height to be resized to the
   * target value. If only one of the 2 scaled dimensions is set, then the image is scaled by preserving its aspect
   * ratio.
   *
   * @param scaledHeight
   *     the scaled height
   */
  public void setScaledHeight(Integer scaledHeight) {
    this.scaledHeight = scaledHeight;
  }

  /**
   * Gets whether this image view can be modified by the user drawing in the screen, e.g. a POD signature.
   *
   * @return the boolean
   */
  @Override
  public boolean isDrawable() {
    return drawable;
  }

  /**
   * Configures whether this image view can be modified by the user drawing in the screen,
   * e.g. a POD signature. Defaults to {@code false}.
   *
   * @param drawable the drawable
   */
  public void setDrawable(boolean drawable) {
    this.drawable = drawable;
  }
}
