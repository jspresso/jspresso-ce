/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.WeakHashMap;

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
public class WeakPropertyChangeSupport implements Serializable {

  private static final long                                        serialVersionUID = 2153623080004651058L;

  /**
   * "listeners" lists all the generic listeners.
   * <p/>
   * This is transient - its state is written in the writeObject method.
   */
  private transient LinkedList<WeakEntry<PropertyChangeListener>>  listeners;

  /**
   * Hashtable for managing listeners for specific properties. Maps property
   * names to WeakPropertyChangeSupport objects.
   */
  private transient WeakHashMap<String, WeakPropertyChangeSupport> children;

  /**
   * The object to be provided as the "source" for any generated events.
   */
  private Object                                                   source;

  private transient ReferenceQueue<PropertyChangeListener> queue = new ReferenceQueue<PropertyChangeListener>();

  /**
   * Constructs a <code>WeakPropertyChangeSupport</code> object.
   * 
   * @param sourceBean
   *          The bean to be given as the source for any events.
   */
  public WeakPropertyChangeSupport(Object sourceBean) {
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
  public synchronized void addPropertyChangeListener(
      PropertyChangeListener listener) {
    if (listeners == null) {
      listeners = new java.util.LinkedList<WeakEntry<PropertyChangeListener>>();
    } else {
      processQueue();
    }
    listeners.add(WeakEntry.create(listener, queue));
  }

  /**
   * Remove a PropertyChangeListener from the listener list. This removes a
   * PropertyChangeListener that was registered for all properties.
   * 
   * @param listener
   *          The PropertyChangeListener to be removed
   */
  public synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    if (listeners == null) {
      return;
    }
    processQueue();
    listeners.remove(WeakEntry.create(listener));
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
  public synchronized void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (children == null) {
      children = new java.util.WeakHashMap<String, WeakPropertyChangeSupport>();
    }
    WeakPropertyChangeSupport child = children.get(propertyName);
    if (child == null) {
      child = new WeakPropertyChangeSupport(source);
      children.put(propertyName, child);
    }
    child.addPropertyChangeListener(listener);
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
    WeakPropertyChangeSupport child = children.get(propertyName);
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
  public void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
      return;
    }

    java.util.LinkedList<WeakEntry<PropertyChangeListener>> targets = null;
    WeakPropertyChangeSupport child = null;
    synchronized (this) {
      if (listeners != null) {
        targets = (java.util.LinkedList<WeakEntry<PropertyChangeListener>>) listeners
            .clone();
      }
      if (children != null && propertyName != null) {
        child = children.get(propertyName);
      }
    }

    PropertyChangeEvent evt = new PropertyChangeEvent(source, propertyName,
        oldValue, newValue);

    if (targets != null) {
      for (int i = 0; i < targets.size(); i++) {
        WeakEntry<PropertyChangeListener> entry = targets.get(i);
        PropertyChangeListener target = entry.get();
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
  public void firePropertyChange(PropertyChangeEvent evt) {
    Object oldValue = evt.getOldValue();
    Object newValue = evt.getNewValue();
    String propertyName = evt.getPropertyName();
    if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
      return;
    }

    java.util.LinkedList<WeakEntry<PropertyChangeListener>> targets = null;
    WeakPropertyChangeSupport child = null;
    synchronized (this) {
      if (listeners != null) {
        targets = (java.util.LinkedList<WeakEntry<PropertyChangeListener>>) listeners
            .clone();
      }
      if (children != null && propertyName != null) {
        child = children.get(propertyName);
      }
    }

    if (targets != null) {
      for (int i = 0; i < targets.size(); i++) {
        WeakEntry<PropertyChangeListener> entry = targets.get(i);
        PropertyChangeListener target = entry.get();
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
      WeakPropertyChangeSupport child = children.get(propertyName);
      if (child != null && child.listeners != null) {
        return !child.listeners.isEmpty();
      }
    }
    return false;
  }

  private synchronized ArrayList<PropertyChangeListener> getPropertyChangeListenersList() {
    ArrayList<PropertyChangeListener> targets = new ArrayList<PropertyChangeListener>();
    if (listeners != null) {
      for (WeakEntry<PropertyChangeListener> entry : listeners) {
        PropertyChangeListener target = entry.get();
        if (target != null) {
          targets.add(target);
        }
      }
    }
    return targets;
  }

  /**
   * @return all of the <code>PropertyChangeListeners</code> added or an empty
   *         array if no listeners have been added
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  public PropertyChangeListener[] getPropertyChangeListeners() {
    return getPropertyChangeListenersList().toArray(
        new PropertyChangeListener[0]);
  }

  /**
   * @param propertyName
   *          propertyName
   * @return all of the <code>PropertyChangeListeners</code> associated with the
   *         named property. If no such listeners have been added, or if
   *         <code>propertyName</code> is null, an empty array is returned.
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
   */
  public synchronized PropertyChangeListener[] getPropertyChangeListeners(
      String propertyName) {
    ArrayList<PropertyChangeListener> targets = new ArrayList<PropertyChangeListener>();
    if (children != null && propertyName != null) {
      WeakPropertyChangeSupport child = children.get(propertyName);
      if (child != null) {
        targets.addAll(child.getPropertyChangeListenersList());
      }
    }
    return targets.toArray(new PropertyChangeListener[0]);
  }

  /**
   * Remove all invalidated entries from the map, that is, remove all entries
   * whose keys have been discarded. This method should be invoked once by each
   * public mutator in this class. We don't invoke this method in public
   * accessors because that can lead to surprising
   * ConcurrentModificationExceptions.
   */
  private void processQueue() {
    WeakEntry<PropertyChangeListener> wk;
    while ((wk = (WeakEntry<PropertyChangeListener>) queue.poll()) != null) {
      listeners.remove(wk);
    }
  }

  private static final class WeakEntry<E> extends WeakReference<E> {

    private int hash; /*
                       * Hashcode of key, stored here since the key may be
                       * tossed by the GC
                       */

    private WeakEntry(E k) {
      super(k);
      hash = k.hashCode();
    }

    private static <E> WeakEntry<E> create(E k) {
      if (k == null) {
        return null;
      }
      return new WeakEntry<E>(k);
    }

    private WeakEntry(E k, ReferenceQueue<? super E> q) {
      super(k, q);
      hash = k.hashCode();
    }

    private static <E> WeakEntry<E> create(E k, ReferenceQueue<? super E> q) {
      if (k == null) {
        return null;
      }
      return new WeakEntry<E>(k, q);
    }

    /*
     * A WeakEntry is equal to another WeakEntry iff they both refer to objects
     * that are, in turn, equal according to their own equals methods
     */
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof WeakEntry)) {
        return false;
      }
      Object t = this.get();
      Object u = ((WeakEntry<?>) o).get();
      if ((t == null) || (u == null)) {
        return false;
      }
      if (t == u) {
        return true;
      }
      return t.equals(u);
    }

    @Override
    public int hashCode() {
      return hash;
    }
  }
}
