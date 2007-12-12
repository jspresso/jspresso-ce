/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import com.d2s.framework.binding.AbstractValueConnector;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.util.Color;

/**
 * This abstract class serves as the base class for all ULCComponent connectors.
 * Subclasses can access the ULCComponent using the parametrized method
 * <code>getConnectedULCComponent()</code> which returns the parametrized type
 * of the class.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            The actual class of the subclass of <code>ULCComponent</code>.
 */
public abstract class ULCComponentConnector<E extends ULCComponent> extends
    AbstractValueConnector {

  private E     connectedULCComponent;

  private Color savedForeground;

  /**
   * Constructs a new <code>ULCComponentConnector</code> instance.
   * 
   * @param id
   *            the connector identifier.
   * @param connectedULCComponent
   *            the connected ULCComponent.
   */
  public ULCComponentConnector(String id, E connectedULCComponent) {
    super(id);
    this.connectedULCComponent = connectedULCComponent;
    bindULCComponent();
    updateState();
  }

  /**
   * Turn read-only if not bound.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isWritable() {
    return (getModelConnector() != null) && super.isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    if (isReadable()) {
      if (savedForeground != null) {
        getConnectedULCComponent().setForeground(savedForeground);
      }
      savedForeground = null;
    } else if (savedForeground == null) {
      savedForeground = getConnectedULCComponent().getForeground();
      getConnectedULCComponent().setForeground(
          getConnectedULCComponent().getBackground());
    }
  }

  /**
   * Attaches the ULCComponent to the connector.
   */
  protected abstract void bindULCComponent();

  /**
   * Gets the connectedULCComponent.
   * 
   * @return the connectedULCComponent.
   */
  protected E getConnectedULCComponent() {
    return connectedULCComponent;
  }
}
