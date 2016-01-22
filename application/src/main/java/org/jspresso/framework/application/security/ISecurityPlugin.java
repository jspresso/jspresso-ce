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

import java.util.Map;

import org.jspresso.framework.security.ISecurable;

/**
 * A delegate plugin used to implement dynamic security algorithm.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public interface ISecurityPlugin {

  /**
   * Checks authorization for secured access.
   *
   * @param securable
   *          the id of the secured access to check.
   * @param context
   *          the security context of the authorization.
   * @return true if access is granted.
   */
  boolean isAccessGranted(ISecurable securable, Map<String, Object> context);

}
