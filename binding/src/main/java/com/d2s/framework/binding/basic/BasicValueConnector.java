/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.basic;

import com.d2s.framework.binding.AbstractValueConnector;

/**
 * This is a simple connector which itself holds the connector's value. This
 * connector is useful for building complex technical view models (e.g.
 * TableModel where each cell model would be an instance of
 * <code>BasicValueConnector</code>).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicValueConnector extends AbstractValueConnector {

  private Object connecteeValue;

  /**
   * Constructs a new instance of BasicValueConnector.
   * 
   * @param id
   *          the connector identifier
   */
  public BasicValueConnector(String id) {
    super(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicValueConnector clone(String newConnectorId) {
    BasicValueConnector clonedConnector = (BasicValueConnector) super
        .clone(newConnectorId);
    clonedConnector.connecteeValue = null;
    return clonedConnector;
  }

  /**
   * Gets the self-hosted value.
   * 
   * @return the self-hosted value.
   */
  @Override
  protected Object getConnecteeValue() {
    return connecteeValue;
  }

  /**
   * Sets the self-hosted value.
   * 
   * @param connecteeValue
   *          the value to host
   */
  @Override
  protected void setConnecteeValue(Object connecteeValue) {
    this.connecteeValue = connecteeValue;
  }
}
