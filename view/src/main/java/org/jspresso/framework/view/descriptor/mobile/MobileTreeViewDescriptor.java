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
 * @author Vincent Vandenschrick
 */
public class MobileTreeViewDescriptor extends AbstractTreeViewDescriptor implements IMobileViewDescriptor {

  private boolean showArrow;

  /**
   * Instantiates a new Mobile tree view descriptor.
   */
  public MobileTreeViewDescriptor() {
    showArrow = true;
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

  /**
   * Is show arrow.
   *
   * @return the boolean
   */
  public boolean isShowArrow() {
    return showArrow;
  }

  /**
   * Sets show arrow.
   *
   * @param showArrow the show arrow
   */
  public void setShowArrow(boolean showArrow) {
    this.showArrow = showArrow;
  }
}
