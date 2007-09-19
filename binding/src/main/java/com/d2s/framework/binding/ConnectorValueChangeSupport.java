/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

/**
 * Helper class to ease the IConnectorValueChangeListener management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorValueChangeSupport {

  private Set<IConnectorValueChangeListener> inhibitedListeners;

  private Set<IConnectorValueChangeListener> listeners;
  private IValueConnector                    source;

  /**
   * Constructs a new Connector change support.
   * 
   * @param sourceConnector
   *            The connector to which this ConnectorValueChangeSupport is
   *            attached. sourceConnector will serve as <code>source</code> of
   *            fired ConnectorValueChangeEvent if no other is provided.
   */
  public ConnectorValueChangeSupport(IValueConnector sourceConnector) {
    if (sourceConnector == null) {
      throw new NullPointerException();
    }
    source = sourceConnector;
  }

  /**
   * Adds a new listener to this connector.
   * 
   * @param listener
   *            The added listener.
   * @see IValueConnector#addConnectorValueChangeListener(IConnectorValueChangeListener)
   */
  public synchronized void addConnectorValueChangeListener(
      IConnectorValueChangeListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new LinkedHashSet<IConnectorValueChangeListener>(8);
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Registers a listener to be excluded (generally temporarily) from the
   * notification process without being removed from the actual listeners
   * collection.
   * 
   * @param listener
   *            the excluded listener.
   */
  public void addInhibitedListener(IConnectorValueChangeListener listener) {
    if (inhibitedListeners == null && listener != null) {
      inhibitedListeners = new HashSet<IConnectorValueChangeListener>(4);
    }
    inhibitedListeners.add(listener);
  }

  /**
   * Propagates the <code>ConnectorValueChangeEvent</code> as is (i.e.
   * whithout modifying its source) to the listeners.
   * 
   * @param evt
   *            the propagated <code>ConnectorValueChangeEvent</code>
   */
  public void fireConnectorValueChange(ConnectorValueChangeEvent evt) {
    if (listeners != null) {
      Object oldValue = evt.getOldValue();
      Object newValue = evt.getNewValue();
      if (ObjectUtils.equals(oldValue, newValue)) {
        return;
      }
      for (IConnectorValueChangeListener listener : new LinkedHashSet<IConnectorValueChangeListener>(
          listeners)) {
        if (inhibitedListeners == null
            || !inhibitedListeners.contains(listener)) {
          if (listeners.contains(listener)) {
            listener.connectorValueChange(evt);
          }
        }
      }
    }
  }

  /**
   * Fires a new <code>ConnectorValueChangeEvent</code> built with
   * <code>source</code> as source and parameters as old and new values.
   * 
   * @param oldValue
   *            The old connector's value
   * @param newValue
   *            The new connector's value
   */
  public void fireConnectorValueChange(Object oldValue, Object newValue) {
    ConnectorValueChangeEvent evt = new ConnectorValueChangeEvent(source,
        oldValue, newValue);
    fireConnectorValueChange(evt);
  }

  /**
   * Gets wether the listener collection is empty.
   * 
   * @return true if the listener collection is empty.
   */
  public boolean isEmpty() {
    return listeners == null || listeners.isEmpty();
  }

  /**
   * Removes a new <code>IConnectorValueChangeListener</code>.
   * 
   * @param listener
   *            The removed listener.
   * @see IValueConnector#removeConnectorValueChangeListener(IConnectorValueChangeListener)
   */
  public synchronized void removeConnectorValueChangeListener(
      IConnectorValueChangeListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
    }
  }

  /**
   * Registers a listener to be re-included to the notification process without
   * being re-added to the actual listeners collection.
   * 
   * @param listener
   *            the previously excluded listener.
   */
  public void removeInhibitedListener(IConnectorValueChangeListener listener) {
    if (inhibitedListeners == null || listener == null) {
      return;
    }
    inhibitedListeners.remove(listener);
  }

  
  /**
   * Gets the listeners.
   * 
   * @return the listeners.
   */
  public Set<IConnectorValueChangeListener> getListeners() {
    return new HashSet<IConnectorValueChangeListener>(listeners);
  }
}
