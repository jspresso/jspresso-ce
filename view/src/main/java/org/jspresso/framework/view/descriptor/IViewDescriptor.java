/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor;

import java.awt.Color;
import java.awt.Font;

import org.jspresso.framework.view.action.IActionable;

/**
 * This public interface is the super-interface of all view descriptors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IViewDescriptor extends ISubViewDescriptor, IActionable {

  /**
   * <code>NONE</code> border constant.
   */
  int NONE   = -1;

  /**
   * <code>SIMPLE</code> border constant.
   */
  int SIMPLE = 1;

  /**
   * <code>TITLED</code> border constant.
   */
  int TITLED = 2;

  /**
   * Gets the background color of this view.
   * 
   * @return this view's foreground color.
   */
  Color getBackground();

  /**
   * Gets the border type used to surround view.
   * 
   * @return the border type :
   *         <li> <code>NONE</code> means no border.
   *         <li> <code>SIMPLE</code> means a simple line border.
   *         <li> <code>TITLED</code> means a titled border. The title will be
   *         the name of the view.
   */
  int getBorderType();

  /**
   * Gets the font of this view.
   * 
   * @return this view's font.
   */
  Font getFont();

  /**
   * Gets the foreground color of this view.
   * 
   * @return this view's foreground color.
   */
  Color getForeground();
}
