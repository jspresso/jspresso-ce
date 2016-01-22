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

import org.jspresso.framework.view.descriptor.ESelectionMode;
import org.jspresso.framework.view.descriptor.basic.AbstractListViewDescriptor;

/**
 * This type of descriptor is used to implement a list view. A list view is a
 * single column, un-editable collection view used to display a collection of
 * components. Each item is displayed using a string representation that can be
 * customized using the {@code renderedProperty} property. List views are
 * rarely used since one might prefer its much more advanced cousin, i.e. the
 * table view.
 * <p>
 * Despite its low usage as an individual UI component, the list view is also
 * used by Jspresso to describe tree parts. A collection of sibling tree nodes
 * can actually be considered as being a list view and can be described as such.
 * In the latter case, the {@code renderedProperty} property will be used
 * to label the tree nodes.
 *
 * @author Vincent Vandenschrick
 */
public class MobileListViewDescriptor extends AbstractListViewDescriptor implements IMobileViewDescriptor {

  private boolean showArrow;

  /**
   * Instantiates a new Mobile list view descriptor.
   */
  public MobileListViewDescriptor() {
    showArrow = true;
    setSelectionMode(ESelectionMode.SINGLE_SELECTION);
  }

  /**
   * Returns {@code false}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isHorizontallyScrollable() {
    return false;
  }

  /**
   * Always false in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isAutoSelectFirstRow() {
    return false;
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setAutoSelectFirstRow(boolean autoSelectFirstRow) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
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
