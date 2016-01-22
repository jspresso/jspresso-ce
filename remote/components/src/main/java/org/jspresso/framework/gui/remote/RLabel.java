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
package org.jspresso.framework.gui.remote;

/**
 * A remote label component.
 *
 * @author Vincent Vandenschrick
 */
public class RLabel extends RTextComponent {

  private static final long serialVersionUID = -7684946402506329656L;
  private String            horizontalAlignment;

  /**
   * Constructs a new {@code RLabel} instance.
   *
   * @param guid
   *          the guid.
   */
  public RLabel(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RLabel} instance. Only used for serialization
   * support.
   */
  public RLabel() {
    // For serialization support
  }

  /**
   * Sets the horizontalAlignment.
   *
   * @param horizontalAlignment
   *          the horizontalAlignment to set.
   */
  public void setHorizontalAlignment(String horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Gets the horizontalAlignment.
   *
   * @return the horizontalAlignment.
   */
  public String getHorizontalAlignment() {
    return horizontalAlignment;
  }
}
