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

import org.jspresso.framework.gui.remote.RTree;

/**
 * A remote mobile tree component.
 *
 * @author Vincent Vandenschrick
 */
public class RMobileTree extends RTree {

  private static final long serialVersionUID = 3702939429412474203L;
  private boolean showArrow;

  /**
   * Constructs a new {@code RMobileTree} instance.
   *
   * @param guid
   *          the guid.
   */
  public RMobileTree(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileTree} instance. Only used for
   * serialization support.
   */
  public RMobileTree() {
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
