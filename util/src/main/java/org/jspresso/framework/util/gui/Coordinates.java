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

/**
 * a simple holder for 2D coordinates.
 *
 * @author Vincent Vandenschrick
 */
public class Coordinates implements Serializable {

  private static final long serialVersionUID = -8402904245208047956L;

  private int               x;
  private int               y;

  /**
   * Constructs a new {@code Coordinates} instance.
   */
  public Coordinates() {
    super();
  }

  /**
   * Constructs a new {@code Coordinates} instance.
   *
   * @param x
   *          the x coordinate.
   * @param y
   *          the y coordinate.
   */
  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coordinates)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Coordinates rhs = (Coordinates) obj;
    return x == rhs.x && y == rhs.y;
  }

  /**
   * Gets the x.
   *
   * @return the x.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the y.
   *
   * @return the y.
   */
  public int getY() {
    return y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return (23 + x * 13) + (31 + y * 17);
  }

  /**
   * Sets the x.
   *
   * @param x
   *          the x to set.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets the y.
   *
   * @param y
   *          the y to set.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getClass().getName() + " : x=" + x + ", y=" + y;
  }

}
