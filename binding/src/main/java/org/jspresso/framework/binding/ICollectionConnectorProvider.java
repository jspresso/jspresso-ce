/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding;

/**
 * Marks objects being able to provide a collection connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICollectionConnectorProvider extends ICompositeValueConnector,
    ICollectionConnectorListProvider {

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  ICollectionConnectorProvider clone();

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *            the identifier of the clone connector
   * @return the connector's clone.
   */
  ICollectionConnectorProvider clone(String newConnectorId);

  /**
   * Gets the collection connector.
   * 
   * @return the collection connector.
   */
  ICollectionConnector getCollectionConnector();
}
