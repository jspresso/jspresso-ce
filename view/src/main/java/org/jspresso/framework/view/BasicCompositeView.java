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
 * Basic composite view.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the type of component this view uses.
 */
public class BasicCompositeView<E> extends BasicView<E> implements
    ICompositeView<E> {

  private List<IView<E>> children;

  /**
   * Constructs a new {@code BasicCompositeView} instance.
   *
   * @param peer
   *          the peer component.
   */
  public BasicCompositeView(E peer) {
    super(peer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IView<E>> getChildren() {
    return children;
  }

  /**
   * Sets the children.
   *
   * @param children
   *          the children to set.
   */
  public void setChildren(List<IView<E>> children) {
    if (this.children != null) {
      for (IView<E> child : this.children) {
        child.setParent(null);
      }
    }
    this.children = children;
    if (this.children != null) {
      for (IView<E> child : this.children) {
        child.setParent(this);
      }
    }
  }
}
