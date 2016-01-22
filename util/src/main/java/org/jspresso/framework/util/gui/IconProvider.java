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
 * Implementations of this interface are designed to provide icons based on an
 * object.
 *
 * @author Vincent Vandenschrick
 */
public interface IconProvider {

  /**
   * Given a user object, this method gives the ability to the tree view
   * descriptor to return the icon used to render the user object. This method
   * may return null.
   *
   * @param userObject
   *          the user object to render.
   * @return the url of the image to use for the renderer or null.
   */
  Icon getIconForObject(Object userObject);
}
