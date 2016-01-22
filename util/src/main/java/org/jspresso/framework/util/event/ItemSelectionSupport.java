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

import gnu.trove.set.hash.TLinkedHashSet;

/**
 * Helper class to ease the IItemSelectionListener management.
 *
 * @author Vincent Vandenschrick
 */
public class ItemSelectionSupport {

  private Set<IItemSelectionListener> listeners;

  /**
   * Adds a new listener to this source.
   *
   * @param listener
   *          The added listener.
   */
  public synchronized void addItemSelectionListener(
      IItemSelectionListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new TLinkedHashSet<>(1);
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Propagates the {@code ItemSelectionEvent} as is (i.e. without
   * modifying its source) to the listeners.
   *
   * @param evt
   *          the propagated {@code ConnectorSelectionEvent}
   */
  public void fireSelectedConnectorChange(ItemSelectionEvent evt) {
    if (listeners != null) {
      for (IItemSelectionListener listener : listeners.toArray(new IItemSelectionListener[listeners.size()])) {
        listener.selectedItemChange(evt);
      }
    }
  }

  /**
   * Removes a new {@code IItemValueChangeListener}.
   *
   * @param listener
   *          The removed listener.
   */
  public synchronized void removeConnectorSelectionListener(
      IItemSelectionListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
      if (listeners.size() == 0) {
        listeners = null;
      }
    }
  }
}
