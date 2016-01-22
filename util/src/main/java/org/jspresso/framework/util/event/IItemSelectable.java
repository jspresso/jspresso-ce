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
package org.jspresso.framework.util.event;

/**
 * This interface is implemented by any structure capable of selecting an item.
 *
 * @author Vincent Vandenschrick
 */
public interface IItemSelectable {

  /**
   * Adds an item selection listener to this object.
   *
   * @param listener
   *          the listener to add.
   */
  void addItemSelectionListener(IItemSelectionListener listener);

  /**
   * Triggers notification of an item selection event. This method has to be
   * made public to cope with notification of the children selection events.
   *
   * @param evt
   *          the event to be propagated.
   */
  void fireSelectedItemChange(ItemSelectionEvent evt);

  /**
   * Retrieves the selected item out of this item selectable or null if none.
   *
   * @param <T>
   *     type inference return.
   * @return the selected item out of this item selectable.
   */
  <T> T getSelectedItem();

  /**
   * Removes an item selection listener from this object.
   *
   * @param listener
   *          the listener to remove.
   */
  void removeItemSelectionListener(IItemSelectionListener listener);
}
