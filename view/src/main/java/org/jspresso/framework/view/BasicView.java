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

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * The basic implementation of the IView interface.
 *
 * @param <E>
 *     the type of component this view uses.
 * @author Vincent Vandenschrick
 */
public class BasicView<E> implements IView<E> {

  private IValueConnector connector;
  private IViewDescriptor descriptor;
  private IView<E>        parent;
  private E               peer;

  /**
   * Constructs a new {@code BasicView} instance.
   *
   * @param peer
   *     the peer component.
   */
  public BasicView(E peer) {
    this.peer = peer;
  }

  /**
   * Returns the {@code IValueConnector} connected to the view.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getConnector() {
    return connector;
  }

  /**
   * Gets the descriptor.
   *
   * @return the descriptor.
   */
  @Override
  public IViewDescriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Gets the parent.
   *
   * @return the parent.
   */
  @Override
  public IView<E> getParent() {
    return parent;
  }

  /**
   * Gets the component responsible for implementing the view.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public E getPeer() {
    return peer;
  }

  /**
   * Returns this view.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public IView<E> getView() {
    return this;
  }

  /**
   * Sets the connector.
   *
   * @param connector
   *     the connector to set.
   */
  @Override
  public void setConnector(IValueConnector connector) {
    this.connector = connector;
  }

  /**
   * Sets the descriptor.
   *
   * @param descriptor
   *     the descriptor to set.
   */
  public void setDescriptor(IViewDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  /**
   * Sets the parent.
   *
   * @param parent
   *     the parent to set.
   */
  @Override
  public void setParent(IView<E> parent) {
    this.parent = parent;
  }

  /**
   * Sets the peer.
   *
   * @param peer
   *     the peer to set.
   */
  @Override
  public void setPeer(E peer) {
    this.peer = peer;
  }

  /**
   * To string.
   *
   * @return original toString completed with user name.
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("descriptor", getDescriptor()).append("peer", getPeer()).append("connector",
        getConnector()).toString();
  }
}
