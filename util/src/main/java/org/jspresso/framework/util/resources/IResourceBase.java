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
package org.jspresso.framework.util.resources;

import java.io.IOException;

/**
 * This interface is implemented by web resources.
 *
 * @author Vincent Vandenschrick
 */
public interface IResourceBase {

  /**
   * Gets the resource mime type.
   *
   * @return the resource mime type.
   */
  String getMimeType();

  /**
   * Gets the name of the resource or null.
   *
   * @return the name of the resource.
   */
  String getName();

  /**
   * Gets the resource length.
   *
   * @return the resource length.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  long getSize() throws IOException;
}
