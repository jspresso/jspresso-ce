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

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * The basic implementation of the IView interface.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the type of component this view uses.
 */
public class BasicView<E> implements IView<E> {

  private IValueConnector connector;
  private IViewDescriptor descriptor;
  private E               peer;

  /**
   * Constructs a new <code>BasicView</code> instance.
   * 
   * @param peer
   *            the peer component.
   */
  public BasicView(E peer) {
    this.peer = peer;
  }

  /**
   * Returns the <code>IValueConnector</code> connected to the swing view.
   * <p>
   * {@inheritDoc}
   */
  public IValueConnector getConnector() {
    return connector;
  }

  /**
   * Gets the descriptor.
   * 
   * @return the descriptor.
   */
  public IViewDescriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Gets the component responsible for implementing the view.
   * <p>
   * {@inheritDoc}
   */
  public E getPeer() {
    return peer;
  }

  /**
   * Returns this view.
   * <p>
   * {@inheritDoc}
   */
  public IView<E> getView() {
    return this;
  }

  /**
   * Sets the connector.
   * 
   * @param connector
   *            the connector to set.
   */
  public void setConnector(IValueConnector connector) {
    this.connector = connector;
  }

  /**
   * Sets the descriptor.
   * 
   * @param descriptor
   *            the descriptor to set.
   */
  public void setDescriptor(IViewDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  /**
   * Sets the peer.
   * 
   * @param peer
   *            the peer to set.
   */
  public void setPeer(E peer) {
    this.peer = peer;
  }

}
