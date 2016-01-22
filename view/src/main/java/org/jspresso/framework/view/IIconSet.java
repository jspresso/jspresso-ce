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
package org.jspresso.framework.view;

import org.jspresso.framework.util.gui.Dimension;

/**
 * Defines the contract of an icon set.
 *
 * @author Vincent Vandenschrick
 */
public interface IIconSet {

  /**
   * Gets the icon image URL for the icon key passed as parameter.
   *
   * @param iconName
   *          the icon name to retrieve the icon for.
   * @param dim
   *          the dimension this iconImage is intended for. You can safely
   *          ignore this parameter, i.e. always return the same dimension
   *          image. In that case, Jspresso will resize the image at runtime.
   * @return the URL based on the icon determination strategy implemented by
   *         this icon set.
   */
  String getIconImageURL(String iconName, Dimension dim);
}
