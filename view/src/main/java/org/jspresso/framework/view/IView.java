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

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This interface establishes the general contract of a view. Its main role is
 * to provide a pair of (visible peer , connector) used by the framework.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          The root class of the view peers.
 */
public interface IView<E> extends IViewProvider<E> {

  /**
   * Gets the view connector of this view. This connector is built connected to
   * the view peer.
   *
   * @return the view connector.
   */
  IValueConnector getConnector();

  /**
   * Gets the descriptor of this view.
   *
   * @return the view descriptor.
   */
  IViewDescriptor getDescriptor();

  /**
   * Gets the parent view or null if it's a root view.
   *
   * @return the parent view or null if it's a root view.
   */
  IView<E> getParent();

  /**
   * Gets the view peer of this view.
   *
   * @return the view peer.
   */
  E getPeer();

  /**
   * Sets the view connector of this view. This connector is built connected to
   * the view peer.
   *
   * @param connector
   *          the view connector.
   */
  void setConnector(IValueConnector connector);

  /**
   * Sets the parent view or null if it's a root view.
   *
   * @param parent
   *          the parent view or null if it's a root view.
   */
  void setParent(IView<E> parent);

  /**
   * Sets the view peer of this view.
   *
   * @param peer
   *          the view peer.
   */
  void setPeer(E peer);
}
