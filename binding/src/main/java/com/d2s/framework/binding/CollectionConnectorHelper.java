/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

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
public final class CollectionConnectorHelper {

  /**
   * Constructs a new CollectionConnectorHelper instance.
   */
  private CollectionConnectorHelper() {
    super();
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
  public static String computeConnectorId(String baseId, int i) {
    return baseId + "[" + i + "]";
  }

  /**
   * Turns on/off lazy children update on collection connectors.
   * 
   * @param connector
   *          the collection connector list provider to work on.
   * @param state
   *          the lazy children update to set.
   * @param recursive
   *          if the connector will be traversed recursively.
   */
  public static void setAllowLazyChildrenLoadingForConnector(
      ICollectionConnectorListProvider connector, boolean state,
      boolean recursive) {
    for (ICollectionConnector childCollectionConnector : connector
        .getCollectionConnectors()) {
      if (recursive) {
        for (String grandChildConnectorKey : childCollectionConnector
            .getChildConnectorKeys()) {
          IValueConnector grandChildConnector = childCollectionConnector
              .getChildConnector(grandChildConnectorKey);
          if (grandChildConnector instanceof ICollectionConnectorListProvider) {
            setAllowLazyChildrenLoadingForConnector(
                (ICollectionConnectorListProvider) grandChildConnector, state,
                recursive);
          }
        }
      }
      childCollectionConnector.setAllowLazyChildrenLoading(state);
    }
  }
}
