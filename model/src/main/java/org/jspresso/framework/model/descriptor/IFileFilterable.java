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
package org.jspresso.framework.model.descriptor;

import java.util.List;
import java.util.Map;

/**
 * This interface is implemented by descriptors of properties which can be
 * filled using files. Typically, binary property descriptors and text property
 * descriptors implement this interface.
 *
 * @author Vincent Vandenschrick
 */
public interface IFileFilterable {

  /**
   * Gets the map of allowed file types (descriptions) and their associated
   * extensions.
   *
   * @return the map of allowed file types (descriptions) and their associated
   *         extensions.
   */
  Map<String, List<String>> getFileFilter();

  /**
   * Returns the default file name to use when saving the content as a file.
   *
   * @return the default file name to use when saving the content as a file.
   */
  String getFileName();

  /**
   * Returns the default content type to use when saving the content as a file.
   *
   * @return the default content type to use when saving the content as a file.
   */
  String getContentType();
}
