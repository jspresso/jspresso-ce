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
 * A remote link component.
 *
 * @author Vincent Vandenschrick
 */
public class RLink extends RLabel implements RActionable {

  private static final long serialVersionUID = 286004875148865037L;

  private RAction           action;

  /**
   * Constructs a new {@code RLink} instance.
   *
   * @param guid
   *          the guid.
   */
  public RLink(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RLink} instance. Only used for serialization
   * support.
   */
  public RLink() {
    // For serialization support
  }

  /**
   * Gets the action.
   *
   * @return the action.
   */
  public RAction getAction() {
    return action;
  }

  /**
   * Sets the action.
   *
   * @param action
   *          the action to set.
   */
  @Override
  public void setAction(RAction action) {
    this.action = action;
  }
}
