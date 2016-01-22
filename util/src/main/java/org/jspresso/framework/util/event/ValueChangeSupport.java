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

import java.util.Collections;
import java.util.Set;

import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;

/**
 * Helper class to ease the IValueChangeListener management.
 *
 * @author Vincent Vandenschrick
 */
public class ValueChangeSupport implements IValueChangeSource {

  private Set<IValueChangeListener> inhibitedListeners;

  private Set<IValueChangeListener> listeners;
  private Object                    source;

  /**
   * Constructs a new value change support.
   *
   * @param source
   *          The source to which this ValueChangeSupport is attached. source
   *          will serve as {@code source} of fired ValueChangeEvent if no
   *          other is provided.
   */
  public ValueChangeSupport(Object source) {
    if (source == null) {
      throw new NullPointerException();
    }
    this.source = source;
  }

  /**
   * Registers a listener to be excluded (generally temporarily) from the
   * notification process without being removed from the actual listeners
   * collection.
   *
   * @param listener
   *          the excluded listener.
   */
  public void addInhibitedListener(IValueChangeListener listener) {
    if (inhibitedListeners == null && listener != null) {
      inhibitedListeners = new THashSet<>(1);
    }
    if (inhibitedListeners != null) {
      inhibitedListeners.add(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addValueChangeListener(IValueChangeListener listener) {
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
   * Fires a new {@code ValueChangeEvent} built with {@code source} as
   * source and parameters as old and new values.
   *
   * @param oldValue
   *          The old connector's value
   * @param newValue
   *          The new connector's value
   */
  public void fireValueChange(Object oldValue, Object newValue) {
    ValueChangeEvent evt = new ValueChangeEvent(source, oldValue, newValue);
    fireValueChange(evt);
  }

  /**
   * Propagates the {@code ValueChangeEvent} as is (i.e. without modifying
   * its source) to the listeners.
   *
   * @param evt
   *          the propagated {@code ValueChangeEvent}
   */
  public void fireValueChange(ValueChangeEvent evt) {
    if (listeners != null && evt.needsFiring()) {
      for (IValueChangeListener listener : listeners.toArray(new IValueChangeListener[listeners.size()])) {
        if (inhibitedListeners == null
            || !inhibitedListeners.contains(listener)) {
          if (listeners.contains(listener)) {
            listener.valueChange(evt);
          }
        }
      }
    }
  }

  /**
   * Gets the listeners.
   *
   * @return the listeners.
   */
  @Override
  public Set<IValueChangeListener> getValueChangeListeners() {
    if (listeners != null) {
      return new TLinkedHashSet<>(listeners);
    }
    return Collections.emptySet();
  }

  /**
   * Gets whether the listener collection is empty.
   *
   * @return true if the listener collection is empty.
   */
  public boolean isEmpty() {
    return listeners == null || listeners.isEmpty();
  }

  /**
   * Registers a listener to be re-included to the notification process without
   * being re-added to the actual listeners collection.
   *
   * @param listener
   *          the previously excluded listener.
   */
  public void removeInhibitedListener(IValueChangeListener listener) {
    if (inhibitedListeners == null || listener == null) {
      return;
    }
    inhibitedListeners.remove(listener);
    if (inhibitedListeners.size() == 0) {
      inhibitedListeners = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void removeValueChangeListener(
      IValueChangeListener listener) {
    if (listeners == null || listener == null) {
      return;
    }
    listeners.remove(listener);
    if (listeners.size() == 0) {
      listeners = null;
    }
  }
}
