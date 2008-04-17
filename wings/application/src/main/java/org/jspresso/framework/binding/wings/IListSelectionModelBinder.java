/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.wings;

import org.jspresso.framework.util.IIndexMapper;
import org.jspresso.framework.util.event.ISelectable;
import org.wings.SListSelectionModel;


/**
 * Helper class used to bind collection view connectors to list selection models
 * (used in <code>SList</code> and <code>STable</code>).
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
      SListSelectionModel selectionModel, IIndexMapper rowMapper);
}
