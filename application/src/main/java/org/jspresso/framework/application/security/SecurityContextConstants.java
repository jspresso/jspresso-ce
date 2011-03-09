/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.security;

/**
 * Well-known action context keys.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class SecurityContextConstants {

  /**
   * <code>MODEL</code>.
   */
  public static final String  MODEL              = "MODEL";

  /**
   * <code>PROPERTY</code>.
   */
  public static final String  PROPERTY           = "PROPERTY";

  /**
   * <code>WORKSPACE</code>.
   */
  public static final String  WORKSPACE          = "WORKSPACE";

  /**
   * <code>MODULE</code>.
   */
  public static final String  MODULE_CHAIN       = "MODULE_CHAIN";

  /**
   * <code>VIEW_CHAIN</code>.
   */
  public static final String  VIEW_CHAIN         = "VIEW_CHAIN";

  /**
   * <code>ACTION_MAP</code>.
   */
  public static final String  ACTION_MAP         = "ACTION_MAP";

  /**
   * <code>ACTION_LIST</code>.
   */
  public static final String  ACTION_LIST        = "ACTION_LIST";

  /**
   * <code>ACTION</code>.
   */
  public static final String  ACTION             = "ACTION";

  /**
   * <code>AUTH_TYPE</code>.
   */
  public static final String  AUTH_TYPE          = "AUTH_TYPE";

  /**
   * <code>AUTH_VISIBLE</code>.
   */
  public static final String  AUTH_VISIBLE       = "AUTH_VISIBLE";

  /**
   * <code>AUTH_ENABLE</code>.
   */
  public static final String  AUTH_ENABLE        = "AUTH_ENABLE";

  /**
   * <code>USER_ID</code>.
   */
  public static final String  USER_ID            = "USER_ID";

  /**
   * <code>USER_ROLES</code>.
   */
  public static final String  USER_ROLES         = "USER_ROLES";

  /**
   * <code>SESSION_PROPERTIES</code>.
   */
  public static final String  SESSION_PROPERTIES = "SESSION_PROPERTIES";

  private SecurityContextConstants() {
    // to prevent this class from being instanciated.
  }
}
