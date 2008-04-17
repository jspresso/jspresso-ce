/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * The basic implementation of the IView interface.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  private IView<E>        parent;
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
   * Gets the parent.
   * 
   * @return the parent.
   */
  public IView<E> getParent() {
    return parent;
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
   * Sets the parent.
   * 
   * @param parent
   *            the parent to set.
   */
  public void setParent(IView<E> parent) {
    this.parent = parent;
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
