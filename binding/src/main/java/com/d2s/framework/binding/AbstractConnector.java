/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.Collection;

import com.d2s.framework.util.IGate;
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
  @Override
  public AbstractConnector clone() {
    return clone(getId());
  }

  /**
   * Returns true if and only if gates are null, empty or all open.
   * 
   * @param gates
   *          the gates collection.
   * @return gates status.
   */
  protected static boolean areGatesOpen(Collection<IGate> gates) {
    if (gates != null) {
      for (IGate gate : gates) {
        if (!gate.isOpen()) {
          return false;
        }
      }
    }
    return true;
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
