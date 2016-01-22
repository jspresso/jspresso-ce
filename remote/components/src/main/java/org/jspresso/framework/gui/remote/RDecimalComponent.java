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
 * A remote number field component.
 *
 * @author Vincent Vandenschrick
 */
public abstract class RDecimalComponent extends RNumericComponent {

  private static final long serialVersionUID = -1920811347497629751L;

  private int               maxFractionDigit;

  /**
   * Constructs a new {@code RIntegerField} instance.
   *
   * @param guid
   *          the guid.
   */
  public RDecimalComponent(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RDecimalComponent} instance. Only used for
   * serialization support.
   */
  public RDecimalComponent() {
    // For serialization support
  }

  /**
   * Gets the maxFractionDigit.
   *
   * @return the maxFractionDigit.
   */
  public int getMaxFractionDigit() {
    return maxFractionDigit;
  }

  /**
   * Sets the maxFractionDigit.
   *
   * @param maxFractionDigit
   *          the maxFractionDigit to set.
   */
  public void setMaxFractionDigit(int maxFractionDigit) {
    this.maxFractionDigit = maxFractionDigit;
  }

}
