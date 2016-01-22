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

import java.util.List;

/**
 * Basic indexed view.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the type of component this view uses.
 */
public class BasicIndexedView<E> extends BasicCompositeView<E> implements
    IIndexedView<E> {

  private int currentViewIndex = -1;

  /**
   * Constructs a new {@code BasicMapView} instance.
   *
   * @param peer
   *          the peer component.
   */
  public BasicIndexedView(E peer) {
    super(peer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IView<E> getChildView(int index) {
    if (index < 0 || super.getChildren() == null) {
      return null;
    }
    return super.getChildren().get(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getCurrentViewIndex() {
    return currentViewIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCurrentViewIndex(int currentViewIndex) {
    this.currentViewIndex = currentViewIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setChildren(List<IView<E>> children) {
    super.setChildren(children);
    if (children != null && children.size() > 0) {
      setCurrentViewIndex(0);
    }
  }
}
