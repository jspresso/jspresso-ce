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
package org.jspresso.framework.util.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

/**
 * This is a utility class that can be used by beans that support bound
 * properties. You can use an instance of this class as a member field of your
 * bean and delegate various work to it.
 * <p/>
 * This class is serializable. When it is serialized it will save (and restore)
 * any listeners that are themselves serializable. Any non-serializable
 * listeners will be skipped during serialization.
 *
 * @author <a href="mailto:haaf@mercatis.de">Holger Engels</a>
 */
public class PropertyChangeSupport implements Serializable {

  private static final long serialVersionUID = 2153623080004651058L;

  /**
   * "listeners" lists all the generic listeners.
   * <p/>
   * This is transient - its state is written in the writeObject method.
   */
  private transient Collection<PropertyChangeListener> listeners;

  /**
   * Hashtable for managing listeners for specific properties. Maps property
   * names to WeakPropertyChangeSupport objects.
   */
  private transient Map<String, PropertyChangeSupport> children;

  /**
   * The object to be provided as the "source" for any generated events.
   */
  private Object source;

  /**
   * Constructs a {@code WeakPropertyChangeSupport} object.
   *
   * @param sourceBean
   *          The bean to be given as the source for any events.
   */
  public PropertyChangeSupport(Object sourceBean) {
    if (sourceBean == null) {
      throw new NullPointerException();
    }
    source = sourceBean;
  }

  /**
   * Add a PropertyChangeListener to the listener list. The listener is
   * registered for all properties.
   *
   * @param listener
   *          The PropertyChangeListener to be added
   */
  public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    if (listeners == null) {
      listeners = createListenersCollection();
    }
    listeners.add(listener);
  }

  /**
   * Create listeners collection.
   *
   * @return the collection
   */
  protected Collection<PropertyChangeListener> createListenersCollection() {
    return new ArrayList<>(1);
  }

  /**
   * Remove a PropertyChangeListener from the listener list. This removes a
   * PropertyChangeListener that was registered for all properties.
   *
   * @param listener
   *          The PropertyChangeListener to be removed
   */
  public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    if (listeners == null) {
      return;
    }
    listeners.remove(listener);
    if (listeners.size() == 0) {
      listeners = null;
    }
  }

  /**
   * Add a PropertyChangeListener for a specific property. The listener will be
   * invoked only when a call on firePropertyChange names that specific
   * property.
   *
   * @param propertyName
   *          The name of the property to listen on.
   * @param listener
   *          The PropertyChangeListener to be added
   */
  public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    if (children == null) {
      children = new THashMap<>(1, 1.0f);
    }
    PropertyChangeSupport child = children.get(propertyName);
    if (child == null) {
      child = createChild();
      children.put(propertyName, child);
    }
    child.addPropertyChangeListener(listener);
  }

  /**
   * Gets source.
   *
   * @return the source
   */
  protected Object getSource() {
    return source;
  }

  /**
   * Create child.
   *
   * @return the property change support
   */
  protected PropertyChangeSupport createChild() {
    return new PropertyChangeSupport(getSource());
  }

  /**
   * Remove a PropertyChangeListener for a specific property.
   *
   * @param propertyName
   *          The name of the property that was listened on.
   * @param listener
   *          The PropertyChangeListener to be removed
   */
  public synchronized void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (children == null) {
      return;
    }
    PropertyChangeSupport child = children.get(propertyName);
    if (child == null) {
      return;
    }
    child.removePropertyChangeListener(listener);
  }

  /**
   * Report a bound property update to any registered listeners. No event is
   * fired if old and new are equal and non-null.
   *
   * @param propertyName
   *          The programmatic name of the property that was changed.
   * @param oldValue
   *          The old value of the property.
   * @param newValue
   *          The new value of the property.
   */
  @SuppressWarnings("unchecked")
  public void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
      return;
    }

    List<PropertyChangeListener> targets = null;
    PropertyChangeSupport child = null;
    synchronized (this) {
      if (listeners != null) {
        targets = new ArrayList<>(listeners);
      }
      if (children != null && propertyName != null) {
        child = children.get(propertyName);
      }
    }

    PropertyChangeEvent evt = new PropertyChangeEvent(source, propertyName,
        oldValue, newValue);

    if (targets != null) {
      for (PropertyChangeListener target : targets) {
        if (target != null) {
          target.propertyChange(evt);
        }
      }
    }

    if (child != null) {
      child.firePropertyChange(evt);
    }
  }

  /**
   * Report an int bound property update to any registered listeners. No event
   * is fired if old and new are equal and non-null.
   * <p/>
   * This is merely a convenience wrapper around the more general
   * firePropertyChange method that takes Object values.
   *
   * @param propertyName
   *          The programmatic name of the property that was changed.
   * @param oldValue
   *          The old value of the property.
   * @param newValue
   *          The new value of the property.
   */
  public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    if (oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Integer.valueOf(oldValue), Integer.valueOf(
        newValue));
  }

  /**
   * Report a boolean bound property update to any registered listeners. No
   * event is fired if old and new are equal and non-null.
   * <p/>
   * This is merely a convenience wrapper around the more general
   * firePropertyChange method that takes Object values.
   *
   * @param propertyName
   *          The programmatic name of the property that was changed.
   * @param oldValue
   *          The old value of the property.
   * @param newValue
   *          The new value of the property.
   */
  public void firePropertyChange(String propertyName, boolean oldValue,
      boolean newValue) {
    if (oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Boolean.valueOf(oldValue),
        Boolean.valueOf(newValue));
  }

  /**
   * Fire an existing PropertyChangeEvent to any registered listeners. No event
   * is fired if the given event's old and new values are equal and non-null.
   *
   * @param evt
   *          The PropertyChangeEvent object.
   */
  @SuppressWarnings("unchecked")
  public void firePropertyChange(PropertyChangeEvent evt) {
    Object oldValue = evt.getOldValue();
    Object newValue = evt.getNewValue();
    String propertyName = evt.getPropertyName();
    if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
      return;
    }

    List<PropertyChangeListener> targets = null;
    PropertyChangeSupport child = null;
    synchronized (this) {
      if (listeners != null) {
        targets = new ArrayList<>(listeners);
      }
      if (children != null && propertyName != null) {
        child = children.get(propertyName);
      }
    }

    if (targets != null) {
      for (PropertyChangeListener target : targets) {
        if (target != null) {
          target.propertyChange(evt);
        }
      }
    }
    if (child != null) {
      child.firePropertyChange(evt);
    }
  }

  /**
   * Check if there are any listeners for a specific property.
   *
   * @param propertyName
   *          the property name.
   * @return true if there are ore or more listeners for the given property
   */
  public synchronized boolean hasListeners(String propertyName) {
    if (listeners != null && !listeners.isEmpty()) {
      // there is a generic listener
      return true;
    }
    if (children != null) {
      PropertyChangeSupport child = children.get(propertyName);
      if (child != null && child.listeners != null) {
        return !child.listeners.isEmpty();
      }
    }
    return false;
  }

  private synchronized ArrayList<PropertyChangeListener> getPropertyChangeListenersList() {
    ArrayList<PropertyChangeListener> targets = new ArrayList<>();
    if (listeners != null) {
      for (PropertyChangeListener target : listeners) {
        if (target != null) {
          targets.add(target);
        }
      }
    }
    return targets;
  }

  /**
   * Gets listeners attached.
   * @return all of the {@code PropertyChangeListeners} added or an empty
   *         array if no listeners have been added
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  public PropertyChangeListener[] getPropertyChangeListeners() {
    ArrayList<PropertyChangeListener> var = getPropertyChangeListenersList();
    return var.toArray(new PropertyChangeListener[var.size()]);
  }

  /**
   * Gets listeners attached to a given property.
   * @param propertyName
   *          propertyName
   * @return all of the {@code PropertyChangeListeners} associated with the
   *         named property. If no such listeners have been added, or if
   *         {@code propertyName} is null, an empty array is returned.
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(String)
   */
  public synchronized PropertyChangeListener[] getPropertyChangeListeners(
      String propertyName) {
    ArrayList<PropertyChangeListener> targets = new ArrayList<>();
    if (children != null && propertyName != null) {
      PropertyChangeSupport child = children.get(propertyName);
      if (child != null) {
        targets.addAll(child.getPropertyChangeListenersList());
      }
    }
    return targets.toArray(new PropertyChangeListener[targets.size()]);
  }
}
