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
 * A remote date field component.
 *
 * @author Vincent Vandenschrick
 */
public class RTimeField extends RComponent {

  private static final long serialVersionUID = 2384664561321144507L;

  private boolean secondsAware;
  private boolean millisecondsAware;
  private String  formatPattern;

  /**
   * Constructs a new {@code RDateField} instance.
   *
   * @param guid
   *     the guid.
   */
  public RTimeField(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RTimeField} instance. Only used for
   * serialization support.
   */
  public RTimeField() {
    // For serialization support
  }

  /**
   * Gets the secondsAware.
   *
   * @return the secondsAware.
   */
  public boolean isSecondsAware() {
    return secondsAware;
  }

  /**
   * Sets the secondsAware.
   *
   * @param secondsAware
   *     the secondsAware to set.
   */
  public void setSecondsAware(boolean secondsAware) {
    this.secondsAware = secondsAware;
  }

  /**
   * Is milliseconds aware boolean.
   *
   * @return the boolean
   */
  public boolean isMillisecondsAware() {
    return millisecondsAware;
  }

  /**
   * Sets milliseconds aware.
   *
   * @param millisecondsAware
   *     the milliseconds aware
   */
  public void setMillisecondsAware(boolean millisecondsAware) {
    this.millisecondsAware = millisecondsAware;
  }

  /**
   * Gets format pattern.
   *
   * @return the format pattern
   */
  public String getFormatPattern() {
    return formatPattern;
  }

  /**
   * Sets format pattern.
   *
   * @param formatPattern
   *     the format pattern
   */
  public void setFormatPattern(String formatPattern) {
    this.formatPattern = formatPattern;
  }
}
