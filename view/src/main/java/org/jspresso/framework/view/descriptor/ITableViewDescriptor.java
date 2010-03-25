/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This public interface is implemented by tabular view descriptors. For
 * instance, the described view can be a swing JTable presenting a collection of
 * java beans.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITableViewDescriptor extends ICollectionViewDescriptor {

  /**
   * Gets the column view descriptors.
   * 
   * @return the column view descriptors.
   */
  List<IPropertyViewDescriptor> getColumnViewDescriptors();

  /**
   * Gets the action triggered when sorting is triggered by the used (if
   * supported by the UI).
   * 
   * @return the action triggered when sorting is triggered by the used (if
   *         supported by the UI).
   */
  IDisplayableAction getSortingAction();

  /**
   * Gets wether this table will adapt it width according the available
   * horizontal space or install a scrollbar.
   * 
   * @return wether this table will adapt it width according the available
   *         horizontal space or install a scrollbar.
   */
  boolean isHorizontallyScrollable();

  /**
   * Gets wether this table rows should be sorted manually.
   * 
   * @return wether this table rows should be sorted manually.
   */
  boolean isSortable();
}
