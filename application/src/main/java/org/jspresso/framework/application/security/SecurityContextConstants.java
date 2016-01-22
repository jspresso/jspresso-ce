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
package org.jspresso.framework.application.security;

/**
 * Well-known action context keys.
 *
 * @author Vincent Vandenschrick
 */
public final class SecurityContextConstants {

  /**
   * {@code MODEL}. References the Entity/Component fully qualified name.
   */
  public static final String MODEL              = "MODEL";

  /**
   * {@code PROPERTY}. References the Entity/Component property name.
   */
  public static final String PROPERTY           = "PROPERTY";

  /**
   * {@code WORKSPACE}. References the workspace permanent Id.
   */
  public static final String WORKSPACE          = "WORKSPACE";

  /**
   * {@code MODULE}. References the list of modules permanent Ids followed
   * to reach the current module.
   */
  public static final String MODULE_CHAIN       = "MODULE_CHAIN";

  /**
   * {@code VIEW_CHAIN}. References the list of view permanent Ids followed
   * to reach the current view.
   */
  public static final String VIEW_CHAIN         = "VIEW_CHAIN";

  /**
   * {@code ACTION_MAP}. References the action map permanent Id.
   */
  public static final String ACTION_MAP         = "ACTION_MAP";

  /**
   * {@code ACTION_LIST}. References the action list permanent Id.
   */
  public static final String ACTION_LIST        = "ACTION_LIST";

  /**
   * {@code ACTION}. References the action permanent Id.
   */
  public static final String ACTION             = "ACTION";

  /**
   * {@code AUTH_TYPE}. References the type of authorization asked for.
   */
  public static final String AUTH_TYPE          = "AUTH_TYPE";

  /**
   * {@code USER_ID}.
   */
  public static final String USER_ID            = "USER_ID";

  /**
   * {@code USER_ROLES}.
   */
  public static final String USER_ROLES         = "USER_ROLES";

  /**
   * {@code SESSION_PROPERTIES}.
   */
  public static final String SESSION_PROPERTIES = "SESSION_PROPERTIES";

  private SecurityContextConstants() {
    // to prevent this class from being instantiated.
  }
}
