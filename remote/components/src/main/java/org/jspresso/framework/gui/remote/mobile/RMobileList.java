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

import org.jspresso.framework.gui.remote.RList;

/**
 * A remote mobile list component.
 *
 * @author Vincent Vandenschrick
 */
public class RMobileList extends RList {

  private static final long serialVersionUID = -1860119417583708817L;
  private boolean showArrow;

  /**
   * Constructs a new {@code RMobileList} instance.
   *
   * @param guid
   *          the guid.
   */
  public RMobileList(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileList} instance. Only used for
   * serialization support.
   */
  public RMobileList() {
    // For serialization support
  }

  /**
   * Is show arrow.
   *
   * @return the boolean
   */
  public boolean isShowArrow() {
    return showArrow;
  }

  /**
   * Sets show arrow.
   *
   * @param showArrow the show arrow
   */
  public void setShowArrow(boolean showArrow) {
    this.showArrow = showArrow;
  }
}
