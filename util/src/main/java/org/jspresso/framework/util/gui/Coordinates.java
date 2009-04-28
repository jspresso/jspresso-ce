/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jspresso.framework.util.exception.NestedRuntimeException;


/**
 * a simple holder for 2D coordinates.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class Coordinates {

  private int x;
  private int y;

  /**
   * Constructs a new <code>Coordinates</code> instance.
   */
  public Coordinates() {
    super();
  }

  /**
   * Constructs a new <code>Coordinates</code> instance.
   * 
   * @param x
   *            the x coordinate.
   * @param y
   *            the y coordinate.
   */
  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    try {
      Coordinates clone = (Coordinates) super.clone();
      return clone;
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
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
    return new EqualsBuilder().append(x, rhs.x).append(y, rhs.y).isEquals();
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
    return new HashCodeBuilder(23, 53).append(x).append(y).toHashCode();
  }

  /**
   * Sets the x.
   * 
   * @param x
   *            the x to set.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets the y.
   * 
   * @param y
   *            the y to set.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("x", x).append("y", y).toString();
  }

}
