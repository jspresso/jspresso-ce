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
package org.jspresso.framework.util.ldap;

/**
 * Constants for Jspresso LDAP attributes naming.
 *
 * @author Vincent Vandenschrick
 */
public final class LdapConstants {

  /**
   * {@code DESCRIPTION_ATTRIBUTE}.
   */
  public static final String DESCRIPTION_ATTRIBUTE           = "description";

  /**
   * {@code DISPLAY_NAME_ATTRIBUTE}.
   */
  public static final String DISPLAY_NAME_ATTRIBUTE          = "displayName";

  /**
   * {@code MAIL_ATTRIBUTE}.
   */
  public static final String MAIL_ATTRIBUTE                  = "mail";

  /**
   * {@code ORGANISATIONAL_UNIT_OBJECTCLASS}.
   */
  public static final String ORGANISATIONAL_UNIT_OBJECTCLASS = "organizationalUnit";

  /**
   * {@code PASSWORD_ATTRIBUTE}.
   */
  public static final String PASSWORD_ATTRIBUTE = "userPassword";

  /**
   * {@code SERIAL_NUMBER_ATTRIBUTE}.
   */
  public static final String SERIAL_NUMBER_ATTRIBUTE         = "serialNumber";

  /**
   * {@code TOP_OBJECTCLASS}.
   */
  public static final String TOP_OBJECTCLASS                 = "top";

  private LdapConstants() {
    // Empty constructor for utility class
  }
}
