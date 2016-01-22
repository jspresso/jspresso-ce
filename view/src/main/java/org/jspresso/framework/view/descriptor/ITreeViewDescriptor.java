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
import org.jspresso.framework.util.gui.IconProvider;

/**
 * This public interface is implemented by any tree view descriptor.
 *
 * @author Vincent Vandenschrick
 */
public interface ITreeViewDescriptor extends IViewDescriptor {

  /**
   * Gets the iconImageURLProvider.
   *
   * @return the iconImageURLProvider.
   */
  IconProvider getIconImageURLProvider();

  /**
   * Gets the item selection action that will be attached to the created tree
   * view.
   *
   * @return the item selection action that will be attached to the created tree
   *         view.
   */
  IAction getItemSelectionAction();

  /**
   * It gets the maximum depth of the tree structure which is mandatory in case
   * of a recursive one.
   *
   * @return the maximum tree structure depth.
   */
  int getMaxDepth();

  /**
   * Gets the root tree level descriptor of this tree view.
   *
   * @return the root tree level descriptor of this tree view.
   */
  ITreeLevelDescriptor getRootSubtreeDescriptor();

  /**
   * Gets the row action that will be attached to the created tree view.
   *
   * @return the row action that will be attached to the created tree view.
   */
  IAction getRowAction();

  /**
   * Gets whether the tree view should be created expanded.
   *
   * @return true if the tree view should be created expanded.
   */
  boolean isExpanded();

  /**
   * Will the tree view show icons for elements.
   *
   * @return {@code true} whenever the tree should show icon.
   */
  boolean isDisplayIcon();

}
