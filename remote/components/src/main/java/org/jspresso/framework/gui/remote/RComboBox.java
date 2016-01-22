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
 * A component containing enumerated values.
 *
 * @author Vincent Vandenschrick
 */
public class RComboBox extends REnumBox {

  private static final long serialVersionUID = -2604499683793881316L;

  private RIcon[]           icons;
  private boolean           readOnly;

  /**
   * Constructs a new {@code RComboBox} instance.
   *
   * @param guid
   *          the guid
   */
  public RComboBox(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RComboBox} instance. Only used for
   * serialization support.
   */
  public RComboBox() {
    // For serialization support
  }

  /**
   * Gets the icons.
   *
   * @return the icons.
   */
  public RIcon[] getIcons() {
    return icons;
  }

  /**
   * Sets the icons.
   *
   * @param icons
   *          the icons to set.
   */
  public void setIcons(RIcon... icons) {
    this.icons = icons;
  }

  /**
   * Gets the readOnly.
   *
   * @return the readOnly.
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Sets the readOnly.
   *
   * @param readOnly
   *          the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }
}
