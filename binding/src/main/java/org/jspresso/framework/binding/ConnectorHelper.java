/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This is a utility class to easily work on connector trees.
 *
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
   *          the connector t start from.
   * @param connectorPath
   *          the path to the looked-up connector
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
   * This methods gets the array of indices of an object collection held as
   * values in a collection connector. if the collection connector does not
   * contain one of the values, the value is simply ignored.
   *
   * @param collectionConnector
   *          the connector to retrieve the indices of.
   * @param values
   *          the searched connector values.
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
   *          the connector to look the tree path for.
   * @return the list of connector identifiers from the root connector to the
   *         looked-up connector.
   */
  public static List<String> getPathToConnector(IValueConnector connector) {
    if (connector == null) {
      return null;
    }
    List<String> path = new ArrayList<>();
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
   *          the connector to look the root parent connector for.
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
