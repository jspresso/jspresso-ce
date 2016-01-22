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

import java.util.Set;

/**
 * Source of value change events.
 *
 * @author Vincent Vandenschrick
 */
public interface IValueChangeSource {

  /**
   * Adds a new listener to this connector.
   *
   * @param listener
   *          The added listener.
   */
  void addValueChangeListener(IValueChangeListener listener);

  /**
   * Removes a new {@code IValueChangeListener}.
   *
   * @param listener
   *          The removed listener.
   */
  void removeValueChangeListener(IValueChangeListener listener);

  /**
   * Gets the registered {@code IValueChangeListener}s.
   *
   * @return the registered {@code IValueChangeListener}s.
   */
  Set<IValueChangeListener> getValueChangeListeners();

}
