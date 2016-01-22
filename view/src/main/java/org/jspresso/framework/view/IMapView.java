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

import org.jspresso.framework.view.descriptor.ICardViewDescriptor;

/**
 * This interface establishes the general contract of a composite view which has
 * child views which are keyed by a string.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 */
public interface IMapView<E> extends IView<E> {

  /**
   * Adds a child view to the map.
   *
   * @param key
   *          the child view key.
   * @param childView
   *          the child view to be added.
   */
  void addToChildrenMap(String key, IView<E> childView);

  /**
   * Gets the child view indexed by the key or null if the key does not exist.
   *
   * @param key
   *          the key to look up the child view.
   * @return the child view or null if none.
   */
  IView<E> getChild(String key);

  /**
   * Gets the currently displayed view.
   *
   * @return the currently displayed view.
   */
  IView<E> getCurrentView();

  /**
   * Sets the currently displayed view.
   *
   * @param currentView
   *          the currently displayed view.
   */
  void setCurrentView(IView<E> currentView);

  /**
   * Refines to ICardViewDescriptor.
   * <p>
   * {@inheritDoc}
   */
  @Override
  ICardViewDescriptor getDescriptor();
}
