/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import javax.swing.JTree;

import org.jspresso.framework.binding.IValueConnector;


/**
 * Helper class used to bind collection view connectors to tree selection models
 * (used in <code>JTree</code>).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   *            the root connector of the connector hierarchy.
   * @param tree
   *            the the tree to bind the selection model of.
   */
  void bindSelectionModel(IValueConnector rootConnector, JTree tree);
}
