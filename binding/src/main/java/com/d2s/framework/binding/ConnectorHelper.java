/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This is a utility class to easily work on connector trees.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ConnectorHelper {

  private ConnectorHelper() {
    // Hidden class.
  }

  /**
   * Given a starting connector and a connector path, this method retrieves a
   * target connector from the connector parent/child tree.
   * 
   * @param rootConnector
   *            the connector t start from.
   * @param connectorPath
   *            the path to the looked-up connector
   * @return the looked-up connector.
   */
  public static IValueConnector getConnectorFromPath(
      IValueConnector rootConnector, List<String> connectorPath) {
    if (connectorPath == null) {
      return null;
    }
    IValueConnector nextConnector = rootConnector;
    for (int i = 1; i < connectorPath.size(); i++) {
      nextConnector = ((ICompositeValueConnector) nextConnector)
          .getChildConnector(connectorPath.get(i));
    }
    return nextConnector;
  }

  /**
   * This methods gets the array of indices of an onbject collection held as
   * values in a collection connector. if the collection connector does not
   * contain one of the values, the value is simply ignored.
   * 
   * @param collectionConnector
   *            the connector to retrieve the indices of.
   * @param values
   *            the searched connector values.
   * @return the array of values indices.
   */
  public static int[] getIndicesOf(ICollectionConnector collectionConnector,
      Collection<?> values) {
    if (values == null || values.isEmpty()) {
      return new int[0];
    }
    int[] tmpArray = new int[values.size()];
    int n = 0;
    for (int i = 0; i < collectionConnector.getChildConnectorCount(); i++) {
      if (values.contains(collectionConnector.getChildConnector(i)
          .getConnectorValue())) {
        tmpArray[n] = i;
        n++;
      }
    }
    if (n == tmpArray.length) {
      return tmpArray;
    }
    int[] indices = new int[n];
    System.arraycopy(tmpArray, 0, indices, 0, n);
    return indices;
  }

  /**
   * Retrieves a connector complete path following the parent/child
   * relationship.
   * 
   * @param connector
   *            the connector to look the tree path for.
   * @return the list of connector identifiers from the root connector to the
   *         looked-up connector.
   */
  public static List<String> getPathToConnector(IValueConnector connector) {
    if (connector == null) {
      return null;
    }
    List<String> path = new ArrayList<String>();
    IValueConnector parentConnector = connector;
    while (parentConnector != null) {
      path.add(parentConnector.getId());
      parentConnector = parentConnector.getParentConnector();
    }
    Collections.reverse(path);
    return path;
  }

  /**
   * Given a connector, this method loops upward to the parent/child
   * relationship to determine the first connector of the hierarchy.
   * 
   * @param connector
   *            the connector to look the root parent connector for.
   * @return the root parent connector.
   */
  public static IValueConnector getRootConnector(IValueConnector connector) {
    if (connector == null) {
      return null;
    }
    IValueConnector parentConnector = connector;
    while (parentConnector.getParentConnector() != null) {
      parentConnector = parentConnector.getParentConnector();
    }
    return parentConnector;
  }
}
