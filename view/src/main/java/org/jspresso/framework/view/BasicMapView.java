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

import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.view.descriptor.ICardViewDescriptor;

/**
 * Basic map view.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the type of component this view uses.
 */
public class BasicMapView<E> extends BasicView<E> implements IMapView<E> {

  private Map<String, IView<E>> childrenMap;
  private IView<E>              currentView;

  /**
   * Constructs a new {@code BasicMapView} instance.
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
  @Override
  public void addToChildrenMap(String key, IView<E> childView) {
    if (childView != null) {
      childView.setParent(this);
    }
    getChildrenMap().put(key, childView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IView<E> getChild(String key) {
    if (childrenMap == null) {
      return null;
    }
    return childrenMap.get(key);
  }

  /**
   * Gets the children view map.
   *
   * @return the children view map.
   */
  protected Map<String, IView<E>> getChildrenMap() {
    if (childrenMap == null) {
      childrenMap = new HashMap<>();
    }
    return childrenMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ICardViewDescriptor getDescriptor() {
    return (ICardViewDescriptor) super.getDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IView<E> getCurrentView() {
    return currentView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCurrentView(IView<E> currentView) {
    this.currentView = currentView;
  }
}
