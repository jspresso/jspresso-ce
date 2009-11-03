/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.ulc;

import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.gui.IIndexMapper;

import com.ulcjava.base.application.ULCListSelectionModel;

/**
 * Helper class used to bind collection view connectors to list selection models
 * (used in <code>ULCList</code> and <code>ULCExtendedTable</code>).
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
