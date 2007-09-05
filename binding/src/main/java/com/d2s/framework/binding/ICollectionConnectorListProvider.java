/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.List;

/**
 * Marks objects being able to provide a list of collection connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICollectionConnectorListProvider extends
    ICompositeValueConnector {

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  ICollectionConnectorListProvider clone();

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *            the identifier of the clone connector
   * @return the connector's clone.
   */
  ICollectionConnectorListProvider clone(String newConnectorId);

  /**
   * Gets the collection connector.
   * 
   * @return the collection connector.
   */
  List<ICollectionConnector> getCollectionConnectors();
}
