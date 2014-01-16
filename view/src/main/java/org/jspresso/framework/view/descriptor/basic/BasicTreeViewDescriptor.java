/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.action.IAction;

/**
 * This descriptor is use to design a tree view. The way to define a tree view
 * in Jspresso is a matter of assembling <i>tree level descriptors</i>
 * hierarchically. A <i>tree level descriptor</i> is a group of sibling nodes
 * that usually represent a component collection property. Each individual tree
 * node collection can be secured by using role-based authorization (i.e.
 * {@code grantedRoles}) on its descriptor.
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTreeViewDescriptor extends AbstractTreeViewDescriptor {

  private boolean                    expanded;
  private IAction                    itemSelectionAction;
  private IAction                    rowAction;

  /**
   * Constructs a new {@code BasicTreeViewDescriptor} instance.
   */
  public BasicTreeViewDescriptor() {
    expanded = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getItemSelectionAction() {
    return itemSelectionAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getRowAction() {
    return rowAction;
  }

  /**
   * Gets the expanded.
   * 
   * @return the expanded.
   */
  @Override
  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Setting this property to {@code true} configures the created tree to
   * appear with its node expanded. A value of {@code false} (default)
   * means that the tree nodes are initially collapsed.
   *
   * @param expanded
   *          the expanded to set.
   */
  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  /**
   * This property allows to bind an action that gets triggered every time the
   * selection changes on the tree view. The action context passed to the action
   * when it is executed is the same as if it had been registered on the tree
   * view.
   * 
   * @param itemSelectionAction
   *          the itemSelectionAction to set.
   */
  public void setItemSelectionAction(IAction itemSelectionAction) {
    this.itemSelectionAction = itemSelectionAction;
  }

  /**
   * Registers an action that is implicitly triggered every time a row is
   * activated (e.g. double-clicked for current UI channels) on the collection
   * view UI peer. The context of the action execution is the same as if the
   * action was registered in the view action map.
   * 
   * @param rowAction
   *          the rowAction to set.
   */
  public void setRowAction(IAction rowAction) {
    this.rowAction = rowAction;
  }
}
