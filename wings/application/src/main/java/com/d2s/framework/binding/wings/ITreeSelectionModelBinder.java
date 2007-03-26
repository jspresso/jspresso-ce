/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import org.wings.STree;

import com.d2s.framework.binding.IValueConnector;

/**
 * Helper class used to bind collection view connectors to tree selection models
 * (used in <code>STree</code>).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITreeSelectionModelBinder {

  /**
   * Binds a collection connector to keep track of a selection model selections.
   * 
   * @param rootConnector
   *          the root connector of the connector hierarchy.
   * @param tree
   *          the the tree to bind the selection model of.
   */
  void bindSelectionModel(IValueConnector rootConnector, STree tree);
}
