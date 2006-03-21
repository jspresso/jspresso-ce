/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is a simple implementation of IConnectorMap. For instance, it may
 * serve for federating all the connectors of a view designed to represent the
 * same model.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorMap implements IConnectorMap {

  private ICompositeValueConnector     parentConnector;
  private Map<String, IValueConnector> storageMap;

  /**
   * Constructs a new <code>ConnectorMap</code> instance.
   * 
   * @param parentConnector
   *          the composite connector holding the connector map.
   */
  public ConnectorMap(ICompositeValueConnector parentConnector) {
    this.parentConnector = parentConnector;
  }

  /**
   * {@inheritDoc}
   */
  public void addConnector(String storageKey, IValueConnector connector) {
    if (storageKey != null && connector != null) {
      getStorageMap().put(storageKey, connector);
    }
    connector.setParentConnector(parentConnector);
  }

  /**
   * {@inheritDoc}
   */
  public void removeConnector(String storageKey) {
    if (storageKey != null) {
      getStorageMap().remove(storageKey);
    }
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getConnector(String connectorId) {
    return getStorageMap().get(connectorId);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getStorageKeys() {
    return getStorageMap().keySet();
  }

  /**
   * @return Returns the storageMap.
   */
  private Map<String, IValueConnector> getStorageMap() {
    if (storageMap == null) {
      storageMap = new LinkedHashMap<String, IValueConnector>(8);
    }
    return storageMap;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasConnectors() {
    return storageMap != null && !storageMap.isEmpty();
  }

  /**
   * Gets the parentConnector.
   * 
   * @return the parentConnector.
   */
  protected ICompositeValueConnector getParentConnector() {
    return parentConnector;
  }
}
