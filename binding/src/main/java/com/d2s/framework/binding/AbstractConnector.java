/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import com.d2s.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * This abstract class holds some default implementation for connector. All the
 * default connectors inherit from this default behaviour.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public abstract class AbstractConnector extends AbstractPropertyChangeCapable
    implements IConnector {

  private String id;

  /**
   * Constructs a new AbstractConnector using an identifier. In case of a bean
   * connector, this identifier must be the bean property the connector
   * connects.
   * 
   * @param id
   *          The connector identifier.
   */
  public AbstractConnector(String id) {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  public AbstractConnector clone(String newConnectorId) {
    AbstractConnector clonedConnector = (AbstractConnector) super.clone();
    clonedConnector.id = newConnectorId;
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Default implementation does nothing.
   * <p>
   * {@inheritDoc}
   */
  public void updateState() {
    // Default empty implementation.
  }
}
