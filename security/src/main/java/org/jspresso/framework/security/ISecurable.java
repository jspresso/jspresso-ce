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
package org.jspresso.framework.security;

import java.util.Collection;

/**
 * Marks a resource as capable of holding security restrictions.
 *
 * @author Vincent Vandenschrick
 */
public interface ISecurable {

  /**
   * Gets the list of roles which are granted access to this resource. If null is
   * returned the access control is disabled. an empty collection means that
   * nobody can access the resource (this might not be very useful).
   *
   * @return list of roles which are granted access to this resource
   */
  Collection<String> getGrantedRoles();
}
