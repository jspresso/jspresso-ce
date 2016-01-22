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

import org.jspresso.framework.util.gui.IconProvider;

/**
 * This public interface is implemented by list view descriptors. The described
 * view will typically be implemented by a swing JList representing a collection
 * of java beans described by one of their property.
 *
 * @author Vincent Vandenschrick
 */
public interface IListViewDescriptor extends ICollectionViewDescriptor {

  /**
   * Gets the iconImageURLProvider.
   *
   * @return the iconImageURLProvider.
   */
  IconProvider getIconImageURLProvider();

  /**
   * Gets the name of the underlying model property which is made visible by
   * the list.
   *
   * @return the name of the underlying model rendered property.
   */
  String getRenderedProperty();

  /**
   * Will the list view show icons for elements.
   *
   * @return {@code true} whenever the list should show icon.
   */
  boolean isDisplayIcon();
}
