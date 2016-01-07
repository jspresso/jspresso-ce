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
package org.jspresso.framework.model.descriptor.basic;

/**
 * Abstract superclass for time-aware property descriptors.
 *
 * @author Vincent Vandenschrick
 */
public abstract class BasicTimeAwarePropertyDescriptor extends BasicScalarPropertyDescriptor {

  private boolean secondsAware;
  private boolean millisecondsAware;

  /**
   * Instantiates a new Basic time aware property descriptor.
   */
  public BasicTimeAwarePropertyDescriptor() {
    secondsAware = true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isSecondsAware() {
    return secondsAware || isMillisecondsAware();
  }

  /**
   * Should this time information include seconds.
   *
   * @param secondsAware
   *          Configure to {@code true} if this time information include
   *          seconds.
   */
  public void setSecondsAware(boolean secondsAware) {
    this.secondsAware = secondsAware;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isMillisecondsAware() {
    return millisecondsAware;
  }

  /**
   * Should this time information include milliseconds.
   *
   * @param millisecondsAware
   *     Configure to {@code true} if this time information include
   *     milliseconds.
   */
  public void setMillisecondsAware(boolean millisecondsAware) {
    this.millisecondsAware = millisecondsAware;
  }
}
