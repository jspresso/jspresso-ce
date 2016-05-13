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
package org.jspresso.framework.view.descriptor.basic;

/**
 * This specialized property view descriptor is used in order to be able to
 * refine the &quot;format&quot; that is used to parse and format dates.
 *
 * @author Vincent Vandenschrick
 */
public class BasicDatePropertyViewDescriptor extends BasicPropertyViewDescriptor {

  private String formatPattern;

  /**
   * Gets format pattern.
   *
   * @return the format pattern
   */
  public String getFormatPattern() {
    return formatPattern;
  }

  /**
   * Sets format pattern. Allows to override the default one.
   *
   * @param formatPattern
   *     the format pattern
   */
  public void setFormatPattern(String formatPattern) {
    this.formatPattern = formatPattern;
  }
}
