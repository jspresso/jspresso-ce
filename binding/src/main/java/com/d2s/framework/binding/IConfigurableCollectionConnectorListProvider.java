/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.List;

/**
 * Interface of a configurable collection connector list provider. Should be
 * used by view factories.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConfigurableCollectionConnectorListProvider extends
    ICompositeValueConnector, ICollectionConnectorListProvider {

  /**
   * Sets the collectionConnectorProviders.
   * 
   * @param collectionConnectorProviders
   *            the collectionConnectorProviders to set.
   */
  void setCollectionConnectorProviders(
      List<ICollectionConnectorProvider> collectionConnectorProviders);
}
