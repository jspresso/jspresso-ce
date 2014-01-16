/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.mobile;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.view.descriptor.basic.AbstractTreeViewDescriptor;

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
public class MobileTreeViewDescriptor extends AbstractTreeViewDescriptor {

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IAction getItemSelectionAction() {
    return null;
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IAction getRowAction() {
    return null;
  }

  /**
   * Always true in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isExpanded() {
    return true;
  }
}
