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
 * This interface is implemented by listeners willing to be notified of an item
 * selection change.
 *
 * @author Vincent Vandenschrick
 */
public interface IItemSelectionListener {

  /**
   * This method is called whenever this listener is to be notified that the
   * selected item has changed.
   *
   * @param event
   *          the event containing the object at the origin of the event and the
   *          selected item inside it.
   */
  void selectedItemChange(ItemSelectionEvent event);
}
