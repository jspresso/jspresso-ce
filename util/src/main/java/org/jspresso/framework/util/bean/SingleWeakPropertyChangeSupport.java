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

import java.beans.PropertyChangeListener;
import java.util.Set;

import org.jspresso.framework.util.collection.TWeakHashSet;

/**
 * This property change support prevents from adding twice the same property
 * change listener.
 * 
 * @version $LastChangedRevision: 3701 $
 * @author Vincent Vandenschrick
 */
public class SingleWeakPropertyChangeSupport extends WeakPropertyChangeSupport {

  private static final long serialVersionUID = -3547472625502417905L;
  private Set<PropertyChangeListener> cachedListeners;

  /**
   * Constructs a new {@code SinglePropertyChangeSupport} instance.
   *
   * @param sourceBean
   *          the source bean.
   */
  public SingleWeakPropertyChangeSupport(Object sourceBean) {
    super(sourceBean);
  }

  /**
   * Checks listener uniqueness before performing.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
    if (checkUniqueness(null, listener)) {
      super.addPropertyChangeListener(listener);
      if (cachedListeners == null) {
        cachedListeners = new TWeakHashSet<>();
      }
      cachedListeners.add(listener);
    }
  }

  /**
   * Checks listener uniqueness before performing.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    if (checkUniqueness(propertyName, listener)) {
      super.addPropertyChangeListener(propertyName, listener);
      if (cachedListeners == null) {
        cachedListeners = new TWeakHashSet<>();
      }
      cachedListeners.add(listener);
    }
  }

  /**
   * Remove property change listener.
   *
   * @param listener the listener
   */
  @Override
  public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
    super.removePropertyChangeListener(listener);
    if (cachedListeners != null) {
      cachedListeners.remove(listener);
      if (cachedListeners.size() == 0) {
        cachedListeners = null;
      }
    }
  }

  /**
   * Remove property change listener.
   *
   * @param propertyName the property name
   * @param listener the listener
   */
  @Override
  public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    super.removePropertyChangeListener(propertyName, listener);
    if (cachedListeners != null) {
      cachedListeners.remove(listener);
      if (cachedListeners.size() == 0) {
        cachedListeners = null;
      }
    }
  }

  private boolean checkUniqueness(String propertyName, PropertyChangeListener listener) {
    /*
    PropertyChangeListener[] containedListeners;
    if (propertyName == null) {
      containedListeners = getPropertyChangeListeners();
    } else {
      containedListeners = getPropertyChangeListeners(propertyName);
    }
    for (PropertyChangeListener containedListener : containedListeners) {
      if (containedListener == listener) {
        return false;
      }
    }
    return true;
    */

    // Performance optimization. See bug #1135
    return cachedListeners == null || !cachedListeners.contains(listener);
  }
}
