/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

/**
 * Basic map view.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the type of component this view uses.
 */
public class BasicMapView<E> extends BasicView<E> implements IMapView<E> {

  private Map<String, IView<E>> childrenMap;

  /**
   * Constructs a new <code>BasicMapView</code> instance.
   * 
   * @param peer
   *          the peer component.
   */
  public BasicMapView(E peer) {
    super(peer);
  }

  /**
   * {@inheritDoc}
   */
  public IView<E> getChild(String key) {
    if (childrenMap == null) {
      return null;
    }
    return childrenMap.get(key);
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, IView<E>> getChildrenMap() {
    if (childrenMap == null) {
      childrenMap = new HashMap<String, IView<E>>();
    }
    return childrenMap;
  }

  /**
   * {@inheritDoc}
   */
  public void addToChildrenMap(String key, IView<E> childView) {
    getChildrenMap().put(key, childView);
  }
}
