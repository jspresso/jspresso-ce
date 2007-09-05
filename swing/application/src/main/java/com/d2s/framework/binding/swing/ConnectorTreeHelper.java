/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreePath;

import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IValueConnector;

/**
 * This is a utility class to help connector tree management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
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
   *            the root connector of the hierarchy. The returned tree path will
   *            start from this connector.
   * @param connector
   *            the connector to look the tree path for.
   * @return the connector's tree path.
   */
  public static TreePath getTreePathForConnector(IValueConnector rootConnector,
      IValueConnector connector) {
    List<IValueConnector> treePath = new ArrayList<IValueConnector>();
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
