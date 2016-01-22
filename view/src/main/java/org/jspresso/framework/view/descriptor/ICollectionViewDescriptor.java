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

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.model.descriptor.IModelDescriptor;

/**
 * This public interface is implemented by any collection view descriptor. For
 * example, a collection view is a table view, a list view, ...
 *
 * @author Vincent Vandenschrick
 */
public interface ICollectionViewDescriptor extends IScrollableViewDescriptor,
    ICollectionViewDescriptorProvider {

  /**
   * Gets the item selection action that will be attached to the created
   * collection view.
   *
   * @return the item selection action that will be attached to the created
   *         collection view.
   */
  IAction getItemSelectionAction();

  /**
   * Gets the action that is registered on the selected collection element.
   *
   * @return the action that is registered on the selected collection element.
   */
  IAction getRowAction();

  /**
   * Gets the type of selection allowed on the collection view.
   *
   * @return the type of selection allowed on the collection view.
   */
  ESelectionMode getSelectionMode();

  /**
   * Gets the descriptor that describes the view used to display and control
   * pagination.
   *
   * @return the descriptor that describes the view used to display and control
   *         pagination.
   */
  IViewDescriptor getPaginationViewDescriptor();

  /**
   * Whether this collection view should auto select 1st row.
   *
   * @return true if this collection view should auto select 1st row.
   */
  boolean isAutoSelectFirstRow();

  /**
   * Gets the model descriptor this view selection descriptor acts on.
   *
   * @return the view selection model descriptor.
   */
  IModelDescriptor getSelectionModelDescriptor();
}
