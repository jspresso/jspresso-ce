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
package org.jspresso.framework.security;

import java.util.Map;

/**
 * This interface is implemented by Jspresso security handlers.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISecurityHandler {

  /**
   * <code>MODEL</code>.
   */
  String MODEL              = "MODEL";

  /**
   * <code>PROPERTY</code>.
   */
  String PROPERTY           = "PROPERTY";

  /**
   * <code>WORKSPACE</code>.
   */
  String WORKSPACE          = "WORKSPACE";

  /**
   * <code>MODULE</code>.
   */
  String MODULE             = "MODULE";

  /**
   * <code>VIEW</code>.
   */
  String VIEW               = "VIEW";

  /**
   * <code>ACTION</code>.
   */
  String ACTION             = "ACTION";

  /**
   * <code>AUTHORIZATION</code>.
   */
  String AUTHORIZATION      = "AUTHORIZATION";

  /**
   * <code>AUTH_VISIBLE</code>.
   */
  String AUTH_VISIBLE       = "AUTH_VISIBLE";

  /**
   * <code>AUTH_ENABLE</code>.
   */
  String AUTH_ENABLE        = "AUTH_ENABLE";

  /**
   * <code>SUBJECT</code>.
   */
  String SESSION_SUBJECT    = "SESSION_SUBJECT";

  /**
   * <code>SESSION_PROPERTIES</code>.
   */
  String SESSION_PROPERTIES = "SESSION_PROPERTIES";

  /**
   * Checks authorization for secured access.
   * 
   * @param securable
   *          the id of the secured access to check.
   * @return true if access is granted.
   */
  boolean isAccessGranted(ISecurable securable);

}
