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

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.gui.IIndexMapper;

/**
 * Helper class used to bind collection view connectors to list selection models
 * (used in {@code JList} and {@code JTable}).
 *
 * @author Vincent Vandenschrick
 */
public interface IListSelectionModelBinder {

  /**
   * Binds a collection connector to keep track of a selection model selections.
   *
   * @param collectionComponent
   *          the collection component to bind the selection model for.
   * @param selectable
   *          the connector to bind.
   * @param selectionModel
   *          the selection model to bind.
   * @param rowMapper
   *          a row indices transformer or null if none.
   */
  void bindSelectionModel(JComponent collectionComponent,
      ISelectable selectable, ListSelectionModel selectionModel,
      IIndexMapper rowMapper);
}
