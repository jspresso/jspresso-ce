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
package org.jspresso.framework.binding.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreePath;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;

/**
 * This is a utility class to help connector tree management.
 *
 * @author Vincent Vandenschrick
 */
public final class ConnectorTreeHelper {

  private ConnectorTreeHelper() {
    // Hidden class.
  }

  /**
   * Retrieves a connector tree path following the parent/child relationship.
   *
   * @param rootConnector
   *          the root connector of the hierarchy. The returned tree path will
   *          start from this connector.
   * @param connector
   *          the connector to look the tree path for.
   * @return the connector's tree path.
   */
  public static TreePath getTreePathForConnector(IValueConnector rootConnector,
      IValueConnector connector) {
    List<IValueConnector> treePath = new ArrayList<>();
    IValueConnector parentConnector = connector;
    while (parentConnector != null && parentConnector != rootConnector) {
      if (!(parentConnector instanceof ICollectionConnector)
          || !(parentConnector.getParentConnector() instanceof ICollectionConnectorProvider)) {
        treePath.add(parentConnector);
      }
      parentConnector = parentConnector.getParentConnector();
    }
    if (parentConnector == rootConnector) {
      treePath.add(parentConnector);
      Collections.reverse(treePath);
      return new TreePath(treePath.toArray());
    }
    return null;
  }
}
