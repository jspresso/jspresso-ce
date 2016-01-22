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
 * A remote duration field component.
 *
 * @author Vincent Vandenschrick
 */
public class RDurationField extends RComponent {

  private static final long serialVersionUID = 5384453705961766246L;

  private long              maxMillis;

  /**
   * Constructs a new {@code RDurationField} instance.
   *
   * @param guid
   *          the guid.
   */
  public RDurationField(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RDurationField} instance. Only used for
   * serialization support.
   */
  public RDurationField() {
    // For serialization support
  }

  /**
   * Gets the maxMillis.
   *
   * @return the maxMillis.
   */
  public long getMaxMillis() {
    return maxMillis;
  }

  /**
   * Sets the maxMillis.
   *
   * @param maxMillis
   *          the maxMillis to set.
   */
  public void setMaxMillis(long maxMillis) {
    this.maxMillis = maxMillis;
  }
}
