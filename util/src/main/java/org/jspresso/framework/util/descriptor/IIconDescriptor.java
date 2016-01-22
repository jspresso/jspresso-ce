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
package org.jspresso.framework.util.descriptor;

import org.jspresso.framework.util.gui.Icon;

/**
 * This interface is implemented by anything which is graphically identifiable
 * by an icon.
 *
 * @author Vincent Vandenschrick
 */
public interface IIconDescriptor extends IDescriptor {

  /**
   * Gets the icon of the descriptor.
   *
   * @return the icon of the descriptor.
   */
  Icon getIcon();

}
