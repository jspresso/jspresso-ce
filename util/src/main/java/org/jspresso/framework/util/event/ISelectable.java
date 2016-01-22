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
 * this interface is implemented by components supporting selection.
 *
 * @author Vincent Vandenschrick
 */
public interface ISelectable {

  /**
   * Adds a new selection listener to this selectable.
   *
   * @param listener
   *          the added listener.
   */
  void addSelectionChangeListener(ISelectionChangeListener listener);

  /**
   * Gets the selected indices in this selectable.
   *
   * @return the indices to set selected.
   */
  int[] getSelectedIndices();

  /**
   * Removes a selection listener from this selectable.
   *
   * @param listener
   *          the removed listener.
   */
  void removeSelectionChangeListener(ISelectionChangeListener listener);

  /**
   * Sets the selected indices in this selectable.
   *
   * @param selectedIndices
   *          the indices to set selected. The leading index is defaulted to the
   *          highest selected one.
   */
  void setSelectedIndices(int... selectedIndices);

  /**
   * Sets the selected indices in this selectable.
   *
   * @param selectedIndices
   *          the indices to set selected.
   * @param leadingIndex
   *          leading selection index.
   */
  void setSelectedIndices(int[] selectedIndices, int leadingIndex);
}
