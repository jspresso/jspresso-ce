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
 * Interface implemented by listeners on selectable selection.
 *
 * @author Vincent Vandenschrick
 */
public interface ISelectionChangeListener {

  /**
   * This method is triggered whenever a selectable the listener is bound to has
   * its selection changed. Ideally this method will only fire when the event
   * {@code oldValue} differs from {@code newValue}.
   *
   * @param evt
   *          The event representing the change. This event will have : <li>
   *          {@code source} set to the selectable which initiated the
   *          event. <li>{@code oldSelection} set to the old selection. <li>
   *          {@code newSelection} set to the new selection.
   */
  void selectionChange(SelectionChangeEvent evt);
}
