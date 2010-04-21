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
package org.jspresso.framework.view;

import java.util.Map;

/**
 * This interface establishes the general contract of a composite view which has
 * child views which are keyed by a string.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 */
public interface IMapView<E> extends ICompositeView<E> {

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
   * Gets the children view map.
   * 
   * @return the children view map.
   */
  Map<String, IView<E>> getChildrenMap();
}
