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
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.util.descriptor.IStylable;
import org.jspresso.framework.util.gate.IGateAccessible;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.action.IActionable;

/**
 * This public interface is the super-interface of all view descriptors.
 *
 * @author Vincent Vandenschrick
 */
public interface IViewDescriptor extends IIconDescriptor, IStylable,
    ISecurable, IActionable, IGateAccessible, IPermIdSource {

  /**
   * Gets the background color of this view as hex string representation argb
   * coded.
   *
   * @return this view's foreground color.
   */
  String getBackground();

  /**
   * Gets the border type used to surround view.
   *
   * @return the border type : <li> {@code NONE} means no border. <li>
   *         {@code SIMPLE} means a simple line border. <li>
   *         {@code TITLED} means a titled border. The title will be the
   *         name of the view.
   */
  EBorderType getBorderType();

  /**
   * Gets the font of this view as string representation. The font is coded
   * {@code [name];[style];[size]}. <li>[name] is the name of the font. <li>
   * [style] is PLAIN, BOLD, ITALIC or a union of BOLD and ITALIC combined with
   * the '|' character, i.e. BOLD|ITALIC. <li>[size] is the size of the font.
   *
   * @return this view's font.
   */
  String getFont();

  /**
   * Gets the foreground color of this view as hex string representation argb
   * coded.
   *
   * @return this view's foreground color.
   */
  String getForeground();

  /**
   * Gets the model descriptor this view descriptor acts on.
   *
   * @return the view model descriptor.
   */
  IModelDescriptor getModelDescriptor();

  /**
   * Gets the preferred dimension of the view peer component.
   *
   * @return the preferred dimension of the view peer component.
   */
  Dimension getPreferredSize();
}
