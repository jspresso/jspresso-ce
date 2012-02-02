/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Helper class to ease the IValueChangeListener management.
 * 
 * @version $LastChangedRevision: 1249 $
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
   *          will serve as <code>source</code> of fired ValueChangeEvent if no
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
      inhibitedListeners = new HashSet<IValueChangeListener>(4);
    }
    inhibitedListeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addValueChangeListener(IValueChangeListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new LinkedHashSet<IValueChangeListener>(8);
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Fires a new <code>ValueChangeEvent</code> built with <code>source</code> as
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
   * Propagates the <code>ValueChangeEvent</code> as is (i.e. whithout modifying
   * its source) to the listeners.
   * 
   * @param evt
   *          the propagated <code>ValueChangeEvent</code>
   */
  public void fireValueChange(ValueChangeEvent evt) {
    if (listeners != null && evt.needsFiring()) {
      for (IValueChangeListener listener : getValueChangeListeners()) {
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
      return new LinkedHashSet<IValueChangeListener>(listeners);
    }
    return Collections.emptySet();
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
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void removeValueChangeListener(
      IValueChangeListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
    }
  }
}
