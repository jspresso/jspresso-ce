/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.Collection;
import java.util.Collections;

/**
 * This class supports the child connectors management. It is used by composite
 * connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ChildConnectorSupport {

  private IConnectorMapProvider connectorMapProvider;

  /**
   * Constructs a new <code>ChildConnectorSupport</code> instance.
   * 
   * @param connectorMapProvider
   *            the provider of the connector map.
   */
  public ChildConnectorSupport(IConnectorMapProvider connectorMapProvider) {
    this.connectorMapProvider = connectorMapProvider;
  }

  /**
   * Support method holding the implementation of
   * {@link ICompositeValueConnector#getChildConnector(String)}.
   * 
   * @param connectorKey
   *            the key indexing the connector.
   * @return the child connector.
   */
  public IValueConnector getChildConnector(String connectorKey) {
    IConnectorMap childConnectors = connectorMapProvider.getConnectorMap();
    if (childConnectors != null) {
      return childConnectors.getConnector(connectorKey);
    }
    return null;
  }

  /**
   * Support method holding the implementation of
   * {@link ICompositeValueConnector#getChildConnectorKeys()}.
   * 
   * @return the child connector ids collection.
   */
  public Collection<String> getChildConnectorKeys() {
    IConnectorMap childConnectors = connectorMapProvider.getConnectorMap();
    if (childConnectors != null) {
      return childConnectors.getStorageKeys();
    }
    return Collections.emptySet();
  }

}
