/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.Collection;

/**
 * This is the interface implemented by connectors nesting other connectors
 * (composite).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICompositeValueConnector extends IValueConnector {

  /**
   * Adds a new child connector to thois composite.
   * 
   * @param childConnector
   *            the added connector.
   */
  void addChildConnector(IValueConnector childConnector);

  /**
   * Are the children connectors readable ?
   * 
   * @return true if readable.
   */
  boolean areChildrenReadable();

  /**
   * Are the children connectors writable ?
   * 
   * @return true if writable.
   */
  boolean areChildrenWritable();

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  ICompositeValueConnector clone();

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *            the identifier of the clone connector
   * @return the connector's clone.
   */
  ICompositeValueConnector clone(String newConnectorId);

  /**
   * Gets a child connector based on its identifier. It should directly delegate
   * to the <code>IConnectorMap</code>.
   * 
   * @param connectorKey
   *            The key indexing the looked-up connector
   * @return The retrieved connector or null if none exists
   */
  IValueConnector getChildConnector(String connectorKey);

  /**
   * Gets the number of child connectors contained in this composite connector.
   * 
   * @return the element connectors count.
   */
  int getChildConnectorCount();

  /**
   * Gets the collection of connectors hosted by this connector map. It should
   * directly delegate to the <code>IConnectorMap</code>.
   * 
   * @return The collection of child connector in this connector.
   */
  Collection<String> getChildConnectorKeys();

}
