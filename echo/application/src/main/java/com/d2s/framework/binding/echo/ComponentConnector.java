/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.Color;
import nextapp.echo2.app.Component;

import com.d2s.framework.binding.AbstractValueConnector;

/**
 * This abstract class serves as the base class for all Echo component
 * connectors. Subclasses can access the Component using the parametrized method
 * <code>getConnectedJComponent()</code> which returns the parametrized type
 * of the class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          The actual class of the subclass of <code>Component</code>.
 */
public abstract class ComponentConnector<E extends Component> extends
    AbstractValueConnector {

  private E     connectedComponent;

  private Color savedForeground;

  /**
   * Constructs a new <code>ComponentConnector</code> instance.
   *
   * @param id
   *          the connector identifier.
   * @param connectedComponent
   *          the connected Component.
   */
  public ComponentConnector(String id, E connectedComponent) {
    super(id);
    this.connectedComponent = connectedComponent;
    bindComponent();
    updateState();
  }

  /**
   * Gets the connectedComponent.
   *
   * @return the connectedComponent.
   */
  protected E getConnectedComponent() {
    return connectedComponent;
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
        getConnectedComponent().setForeground(savedForeground);
      }
      savedForeground = null;
    } else if (savedForeground == null) {
      savedForeground = getConnectedComponent().getForeground();
      getConnectedComponent().setForeground(
          getConnectedComponent().getBackground());
    }
  }

  /**
   * Attaches the JComponent to the connector.
   */
  protected abstract void bindComponent();
}
