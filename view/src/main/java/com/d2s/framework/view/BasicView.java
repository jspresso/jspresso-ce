/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * The basic implementation of the IView interface.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the type of component this view uses.
 */
public class BasicView<E> implements IView<E> {

  private E               peer;
  private IValueConnector connector;
  private IView<E>        parent;
  private IViewDescriptor descriptor;

  /**
   * Constructs a new <code>BasicView</code> instance.
   * 
   * @param peer
   *          the peer component.
   */
  public BasicView(E peer) {
    this.peer = peer;
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
   * Sets the peer.
   * 
   * @param peer
   *          the peer to set.
   */
  public void setPeer(E peer) {
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
   * Sets the connector.
   * 
   * @param connector
   *          the connector to set.
   */
  public void setConnector(IValueConnector connector) {
    this.connector = connector;
  }

  /**
   * Gets the parent.
   * 
   * @return the parent.
   */
  public IView<E> getParent() {
    return parent;
  }

  /**
   * Sets the parent.
   * 
   * @param parent
   *          the parent to set.
   */
  public void setParent(IView<E> parent) {
    this.parent = parent;
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
   * Sets the descriptor.
   * 
   * @param descriptor
   *          the descriptor to set.
   */
  public void setDescriptor(IViewDescriptor descriptor) {
    this.descriptor = descriptor;
  }

  /**
   * Returns this view.
   * <p>
   * {@inheritDoc}
   */
  public IView<E> getView() {
    return this;
  }

}
