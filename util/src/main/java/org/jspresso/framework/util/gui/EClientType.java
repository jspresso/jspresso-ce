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
package org.jspresso.framework.util.gui;

/**
 * An enumeration for identifying the session client type.
 *
 * @author Vincent Vandenschrick
 */
public enum EClientType {
  /**
   * Swing desktop client.
   */
  DESKTOP_SWING,

  /**
   * Flex desktop client.
   */
  DESKTOP_FLEX,

  /**
   * HTML5 desktop client.
   */
  DESKTOP_HTML5,

  /**
   * HTML5 mobile tablet.
   */
  MOBILE_HTML5_TABLET,

  /**
   * HTML5 mobile phone.
   */
  MOBILE_HTML5_PHONE,

  /**
   * Undefined.
   */
  UNDEFINED;

  /**
   * Is tablet.
   *
   * @return the boolean
   */
  public boolean isTablet() {
    return this == MOBILE_HTML5_TABLET;
  }

  /**
   * Is mobile.
   *
   * @return the boolean
   */
  public boolean isMobile() {
    return this == MOBILE_HTML5_TABLET || this == MOBILE_HTML5_PHONE;
  }

  /**
   * Is desktop.
   *
   * @return the boolean
   */
  public boolean isDesktop() {
    return this == DESKTOP_FLEX || this == DESKTOP_HTML5 || this == DESKTOP_SWING;
  }

  /**
   * Is HTML 5.
   *
   * @return the boolean
   */
  public boolean isHTML5() {
    return this == MOBILE_HTML5_PHONE || this == MOBILE_HTML5_TABLET || this == DESKTOP_HTML5;
  }

}
