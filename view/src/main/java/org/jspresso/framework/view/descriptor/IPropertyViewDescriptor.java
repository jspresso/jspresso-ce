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

import java.util.List;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.util.gui.IClientTypeAware;

/**
 * This public interface is implemented by view descriptors which are just
 * presenting a property.
 *
 * @author Vincent Vandenschrick
 */
public interface IPropertyViewDescriptor extends IViewDescriptor, IClientTypeAware {

  /**
   * Gets the label background.
   *
   * @return the label background.
   */
  String getLabelBackground();

  /**
   * Gets the label font.
   *
   * @return the label font.
   */
  String getLabelFont();

  /**
   * Gets the label foreground.
   *
   * @return the label foreground.
   */
  String getLabelForeground();

  /**
   * Gets the child properties to display in case of a complex property.
   *
   * @return The list of displayed properties in the case of a complex property.
   */
  List<String> getRenderedChildProperties();

  /**
   * Gets the sortability.
   *
   * @return the sortable.
   */
  boolean isSortable();

  /**
   * Gets the default child properties to display in case of a complex property.
   *
   * @return The list of displayed properties in the case of a complex property.
   */
  List<String> getDefaultRenderedChildProperties();

  /**
   * When used in a component descriptor, gets the number of columns a property
   * spans when displayed.
   *
   * @return the spanned column count.
   */
  Integer getWidth();

  /**
   * Returns the action to trigger whenever the property is actioned, e.g.
   * clicked.
   *
   * @return the action to trigger whenever the property is actioned, e.g.
   *         clicked.
   */
  IAction getAction();

  /**
   * Returns the horizontal alignment used for displaying the property.
   *
   * @return the horizontal alignment used for displaying the property.
   */
  EHorizontalAlignment getHorizontalAlignment();

  /**
   * Gets label horizontal position.
   *
   * @return the label horizontal position
   */
  EHorizontalPosition getLabelHorizontalPosition();

}
