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
package org.jspresso.framework.gui.remote.mobile;

import org.jspresso.framework.gui.remote.RBorderContainer;

/**
 * A remote mobile border container component.
 *
 * @author Vincent Vandenschrick
 */
public class RMobileBorderContainer extends RBorderContainer {

  private static final long serialVersionUID = 2672012641048981941L;
  private String position;

  /**
   * Constructs a new {@code RMobileBorderContainer} instance.
   *
   * @param guid           the guid.
   */
  public RMobileBorderContainer(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileBorderContainer} instance. Only used for
   * serialization support.
   */
  public RMobileBorderContainer() {
    // For serialization support
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  public String getPosition() {
    return position;
  }

  /**
   * Sets position.
   *
   * @param position the position
   */
  public void setPosition(String position) {
    this.position = position;
  }
}
