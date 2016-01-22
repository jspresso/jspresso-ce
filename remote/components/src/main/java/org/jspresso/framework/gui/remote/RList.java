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
 * A remote list component.
 *
 * @author Vincent Vandenschrick
 */
public class RList extends RCollectionComponent {

  private static final long serialVersionUID = -1926261840192373120L;

  private boolean displayIcon;

  /**
   * Constructs a new {@code RList} instance.
   *
   * @param guid
   *          the guid.
   */
  public RList(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RList} instance. Only used for serialization
   * support.
   */
  public RList() {
    // For serialization support
  }

  /**
   * Is display icon.
   *
   * @return the boolean
   */
  public boolean isDisplayIcon() {
    return displayIcon;
  }

  /**
   * Sets display icon.
   *
   * @param displayIcon the display icon
   */
  public void setDisplayIcon(boolean displayIcon) {
    this.displayIcon = displayIcon;
  }
}
