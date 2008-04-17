/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding;

import java.util.Collection;

/**
 * This represents a map of connectors indexed by their identifier. The
 * objective of this public interface Is to federate a set of connectors
 * belonging to the same system (a form view for instance providing a connector
 * for each control). This mecanism prevents wiring one by one connectors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnectorMap {

  /**
   * Adds a connector to the connector map.
   * 
   * @param storageKey
   *            the key used to store the connector in the map.
   * @param connector
   *            The added connector
   */
  void addConnector(String storageKey, IValueConnector connector);

  /**
   * Gets a connector based on its storage key.
   * 
   * @param storageKey
   *            The identifier of the looked-up connector
   * @return The retrieved connector or null if none exists
   */
  IValueConnector getConnector(String storageKey);

  /**
   * Gets the collection of connector storage keys hosted by this connector map.
   * 
   * @return The collection of connector storage keys hosted by this map.
   */
  Collection<String> getStorageKeys();

  /**
   * Tells wether this connector map holds connectors.
   * 
   * @return true if this map holds connectors.
   */
  boolean hasConnectors();

  /**
   * Removes a connector from the connector map.
   * 
   * @param storageKey
   *            The removed connector storage key.
   */
  void removeConnector(String storageKey);
}
