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
package org.jspresso.framework.view;

/**
 * This interface establishes the general contract of a composite view which
 * owns indexed child views.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 */
public interface IIndexedView<E> extends ICompositeView<E> {

  /**
   * Gets the indexed child view.
   *
   * @param index
   *          the index to look up the child view.
   * @return the child view or null if none.
   */
  IView<E> getChildView(int index);

  /**
   * Gets the currently displayed view index.
   *
   * @return the currently displayed view.
   */
  int getCurrentViewIndex();

  /**
   * Sets the currently displayed view index.
   *
   * @param index
   *          the currently displayed view.
   */
  void setCurrentViewIndex(int index);
}
