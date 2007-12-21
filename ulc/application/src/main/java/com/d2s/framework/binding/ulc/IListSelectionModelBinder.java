/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import com.d2s.framework.util.IIndexMapper;
import com.d2s.framework.util.event.ISelectable;
import com.ulcjava.base.application.ULCListSelectionModel;

/**
 * Helper class used to bind collection view connectors to list selection models
 * (used in <code>ULCList</code> and <code>ULCExtendedTable</code>).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   *            the connector to bind.
   * @param selectionModel
   *            the selection model to bind.
   * @param rowMapper
   *            a row indices transformer or null if none.
   */
  void bindSelectionModel(ISelectable selectable,
      ULCListSelectionModel selectionModel, IIndexMapper rowMapper);
}
