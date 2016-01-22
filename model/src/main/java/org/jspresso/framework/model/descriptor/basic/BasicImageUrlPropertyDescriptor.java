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
package org.jspresso.framework.model.descriptor.basic;

import org.jspresso.framework.model.descriptor.IImageUrlPropertyDescriptor;

/**
 * Describes an image URL property. This type of descriptor instructs Jspresso
 * to use an image component to interact with this type of property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicImageUrlPropertyDescriptor extends
    BasicStringPropertyDescriptor implements IImageUrlPropertyDescriptor {

  private Integer scaledWidth;
  private Integer scaledHeight;

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getScaledWidth() {
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
   * Returns {@code false}.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultSortablility() {
    return false;
  }
}
