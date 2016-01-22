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
 * A remote check box component.
 *
 * @author Vincent Vandenschrick
 */
public class RCheckBox extends RComponent {

  private static final long serialVersionUID = -5911137476479351628L;
  private boolean           triState;

  /**
   * Constructs a new {@code RCheckBox} instance.
   *
   * @param guid
   *          the guid.
   */
  public RCheckBox(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RCheckBox} instance. Only used for
   * serialization support.
   */
  public RCheckBox() {
    // For serialization support
  }

  /**
   * Gets the triState.
   *
   * @return the triState.
   */
  public boolean isTriState() {
    return triState;
  }

  /**
   * Sets the triState.
   *
   * @param triState
   *          the triState to set.
   */
  public void setTriState(boolean triState) {
    this.triState = triState;
  }

}
