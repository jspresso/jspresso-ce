/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Helper class to ease the IConnectorSelectionListener management.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorSelectionSupport {

  private Set<IConnectorSelectionListener> listeners;

  /**
   * Adds a new listener to this connector.
   * 
   * @param listener
   *            The added listener.
   * @see IConnectorSelector#addConnectorSelectionListener(IConnectorSelectionListener)
   */
  public synchronized void addConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new LinkedHashSet<IConnectorSelectionListener>();
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Propagates the <code>ConnectorSelectionEvent</code> as is (i.e. whithout
   * modifying its source) to the listeners.
   * 
   * @param evt
   *            the propagated <code>ConnectorSelectionEvent</code>
   */
  public void fireSelectedConnectorChange(ConnectorSelectionEvent evt) {
    if (listeners != null) {
      for (IConnectorSelectionListener listener : new LinkedHashSet<IConnectorSelectionListener>(
          listeners)) {
        listener.selectedConnectorChange(evt);
      }
    }
  }

  /**
   * Removes a new <code>IConnectorValueChangeListener</code>.
   * 
   * @param listener
   *            The removed listener.
   * @see IConnectorSelector#removeConnectorSelectionListener(IConnectorSelectionListener)
   */
  public synchronized void removeConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
    }
  }
}
