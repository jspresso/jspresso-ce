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
 * a simple holder for 2D dimension.
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
 * @version $LastChangedRevision: 1249 $
 * @author Vincent Vandenschrick
 */
public class Dimension {

  private int width;
  private int height;

  /**
   * Constructs a new <code>Dimension</code> instance.
   */
  public Dimension() {
    super();
  }

  /**
   * Constructs a new <code>Dimension</code> instance.
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
  public Object clone() {
    try {
      Dimension clone = (Dimension) super.clone();
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
    if (!(obj instanceof Dimension)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Dimension rhs = (Dimension) obj;
    return new EqualsBuilder().append(width, rhs.width).append(height,
        rhs.height).isEquals();
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
   * Gets the height.
   * 
   * @return the height.
   */
  public int getHeight() {
    return height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(23, 53).append(width).append(height)
        .toHashCode();
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
   * Sets the height.
   * 
   * @param height
   *          the height to set.
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("width", width).append("height",
        height).toString();
  }

}
