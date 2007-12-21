/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.basic;

import com.d2s.framework.binding.AbstractCollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IMvcBinder;

/**
 * This is a simple collection connector which itself holds the connector's
 * value and which allows child connectors. This connector is useful for
 * building complex technical view models (e.g. TableModel where the underlying
 * structure would be a <code>BasicCollectionConnector</code>).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCollectionConnector extends AbstractCollectionConnector {

  private Object connecteeValue;

  /**
   * Constructs a new instance of BasicCollectionConnector.
   * 
   * @param id
   *            the connector identifier.
   * @param binder
   *            the IMvcBinder used to bind the dynamically created connectors
   *            of the collection.
   * @param childConnectorPrototype
   *            the connector prototype used to create new instances of child
   *            connectors.
   */
  public BasicCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype) {
    super(id, binder, childConnectorPrototype);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnector clone(String newConnectorId) {
    BasicCollectionConnector clonedConnector = (BasicCollectionConnector) super
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
   *            the value to host
   */
  @Override
  protected void setConnecteeValue(Object connecteeValue) {
    this.connecteeValue = connecteeValue;
    if (getConnecteeValue() == null) {
      updateChildConnectors();
    }
  }
}
