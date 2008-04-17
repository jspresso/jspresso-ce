/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import javax.swing.ListSelectionModel;

import org.jspresso.framework.util.IIndexMapper;
import org.jspresso.framework.util.event.ISelectable;


/**
 * Helper class used to bind collection view connectors to list selection models
 * (used in <code>JList</code> and <code>JTable</code>).
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
      ListSelectionModel selectionModel, IIndexMapper rowMapper);
}
