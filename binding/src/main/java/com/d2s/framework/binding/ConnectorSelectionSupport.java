/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Helper class to ease the IConnectorSelectionListener management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
