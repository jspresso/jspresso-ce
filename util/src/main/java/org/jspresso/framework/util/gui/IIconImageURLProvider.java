/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * Implementations of this interface are designed to provide image urls based on
 * an object.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IIconImageURLProvider {

  /**
   * Gets the image url for the user object passed as parameter.
   * 
   * @param userObject
   *            the user object to get the image url for.
   * @return the looked up image url or null if none.
   */
  String getIconImageURLForObject(Object userObject);
}
