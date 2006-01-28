/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * Interface of a configurable collection connector provider. Should be used by
 * view factories.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConfigurableCollectionConnectorProvider extends
    ICompositeValueConnector, ICollectionConnectorProvider {

  /**
   * Sets the collectionConnector.
   * 
   * @param collectionConnectorProvider
   *          the collectionConnectorProvider to set.
   */
  void setCollectionConnectorProvider(
      ICollectionConnectorProvider collectionConnectorProvider);
}
