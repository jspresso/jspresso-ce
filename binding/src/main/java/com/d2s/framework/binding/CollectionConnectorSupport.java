/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a helper class to ease collection connector management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @see com.d2s.framework.binding.bean.BeanCollectionPropertyConnector
 * @see com.d2s.framework.binding.AbstractCollectionConnector
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorSupport {

  private Map<String, IValueConnector> connectorCache;

  /**
   * Constructs a new CollectionConnectorSupport instance.
   */
  public CollectionConnectorSupport() {
    super();
  }

  /**
   * Removes a connector from the cache.
   * 
   * @param connector
   *          the connector to be removed
   */
  public void uncacheConnector(IValueConnector connector) {
    if (connectorCache == null) {
      return;
    }
    connectorCache.remove(connector.getId());
  }

  /**
   * Adds a connector to the cache.
   * 
   * @param connector
   *          the connector to be added
   */
  public void cacheConnector(IValueConnector connector) {
    if (connectorCache == null) {
      connectorCache = new HashMap<String, IValueConnector>();
    }
    connectorCache.put(connector.getId(), connector);
  }

  /**
   * Gets a connector from the cache.
   * 
   * @param connectorId
   *          the identifier of the looked-up connector.
   * @return the cached connector or null if it does not exist.
   */
  public IValueConnector getCachedConnector(String connectorId) {
    if (connectorCache == null) {
      return null;
    }
    return connectorCache.get(connectorId);
  }

  /**
   * Builds a connector id from a base id and an index. This serves as child
   * connector ids in collection connectors. As of now it returns "baseId[i]"
   * 
   * @param baseId
   *          the base identifier to be used to compose the new connector id.
   * @param i
   *          the index used.
   * @return the created connector id.
   */
  public String computeConnectorId(String baseId, int i) {
    return baseId + "[" + i + "]";
  }

  /**
   * Resets the cache of child connectors.
   * 
   * @param mvcBinder
   *          the mvc binder used to unbind the cached connectors.
   */
  public void clearConnectorCache(IMvcBinder mvcBinder) {
    if (connectorCache != null) {
      for (IValueConnector cachedConnector : connectorCache.values()) {
        if (cachedConnector.getModelConnector() != null) {
          mvcBinder.bind(cachedConnector, null);
        }
      }
    }
    connectorCache = null;
  }
}
