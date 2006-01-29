/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import javax.swing.ListSelectionModel;

import com.d2s.framework.util.IIndexMapper;
import com.d2s.framework.util.event.ISelectable;

/**
 * Helper class used to bind collection view connectors to list selection models
 * (used in <code>JList</code> and <code>JTable</code>).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IListSelectionModelBinder {

  /**
   * Binds a collection connector to keep track of a selection model selections.
   * 
   * @param selectable
   *          the connector to bind.
   * @param selectionModel
   *          the selection model to bind.
   * @param rowMapper
   *          a row indices transformer or null if none.
   */
  void bindSelectionModel(ISelectable selectable,
      ListSelectionModel selectionModel, IIndexMapper rowMapper);
}
