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
 * A remote color field component.
 *
 * @author Vincent Vandenschrick
 */
public class RColorField extends RComponent {

  private static final long serialVersionUID = 3234693770672598145L;

  private String            defaultColor;
  private boolean           resetEnabled;

  /**
   * Constructs a new {@code RColorField} instance.
   *
   * @param guid
   *          the guid.
   */
  public RColorField(String guid) {
    super(guid);
    setResetEnabled(true);
  }

  /**
   * Constructs a new {@code RColorField} instance. Only used for
   * serialization support.
   */
  public RColorField() {
    // For serialization support
  }

  /**
   * Gets the defaultColor.
   *
   * @return the defaultColor.
   */
  public String getDefaultColor() {
    return defaultColor;
  }

  /**
   * Sets the defaultColor.
   *
   * @param defaultColor
   *          the defaultColor to set.
   */
  public void setDefaultColor(String defaultColor) {
    this.defaultColor = defaultColor;
  }

  /**
   * Gets the resetEnabled.
   *
   * @return the resetEnabled.
   */
  public boolean isResetEnabled() {
    return resetEnabled;
  }

  /**
   * Sets the resetEnabled.
   *
   * @param resetEnabled the resetEnabled to set.
   */
  public void setResetEnabled(boolean resetEnabled) {
    this.resetEnabled = resetEnabled;
  }
}
