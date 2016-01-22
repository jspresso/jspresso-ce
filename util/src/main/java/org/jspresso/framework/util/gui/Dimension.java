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
package org.jspresso.framework.util.gui;

import java.io.Serializable;

import org.jspresso.framework.util.lang.ICloneable;

/**
 * a simple holder for 2D dimension.
 *
 * @author Vincent Vandenschrick
 */
public class Dimension implements Serializable, ICloneable {

  private static final long serialVersionUID = -2769276185108835884L;

  private int               height;
  private int               width;

  /**
   * Constructs a new {@code Dimension} instance.
   */
  public Dimension() {
    super();
    width = 0;
    height = 0;
  }

  /**
   * Constructs a new {@code Dimension} instance.
   *
   * @param width
   *          the width.
   * @param height
   *          the height.
   */
  public Dimension(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Dimension)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Dimension rhs = (Dimension) obj;
    return width == rhs.width && height == rhs.height;
  }

  /**
   * Gets the height.
   *
   * @return the height.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the width.
   *
   * @return the width.
   */
  public int getWidth() {
    return width;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return (23 + width * 13) + (31 + height * 17);
  }

  /**
   * Sets the height.
   *
   * @param height
   *          the height to set.
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Sets the width.
   *
   * @param width
   *          the width to set.
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getClass().getName() + " : width=" + width + ", height=" + height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension clone() {
    try {
      return (Dimension) super.clone();
    } catch (CloneNotSupportedException ex) {
      // Cannot happen.
      return null;
    }
  }

}
