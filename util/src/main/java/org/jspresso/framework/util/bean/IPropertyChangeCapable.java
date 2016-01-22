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

import java.beans.PropertyChangeListener;

/**
 * This class needs to be implemented by java beans which are capable of
 * registering a {@code PropertyChangeListener}. See also java beans
 * specifications.
 *
 * @see java.beans.PropertyChangeSupport
 * @author Vincent Vandenschrick
 */
public interface IPropertyChangeCapable {

  /**
   * {@code UNKNOWN} value of property. This value may be passed as
   * {@code newValue} in {@code PropertyChangeEvents}.
   */
  Object UNKNOWN = new Object();

  /**
   * Adds a new {@code PropertyChangeListener}.
   *
   * @param listener
   *          The added listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
   */
  void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Adds a new {@code PropertyChangeListener} using a weak reference.
   *
   * @param listener
   *          The added listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(PropertyChangeListener)
   */
  void addWeakPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Adds a new {@code PropertyChangeListener} on a specific property.
   *
   * @param propertyName
   *          The listened property.
   * @param listener
   *          The added listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String,
   *      PropertyChangeListener)
   */
  void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

  /**
   * Adds a new {@code PropertyChangeListener} on a specific property using
   * a weak reference.
   *
   * @param propertyName
   *          The listened property.
   * @param listener
   *          The added listener.
   * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String,
   *      PropertyChangeListener)
   */
  void addWeakPropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

  /**
   * Removes a new {@code PropertyChangeListener}.
   *
   * @param listener
   *          The removed listener.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(PropertyChangeListener)
   */
  void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * Removes a {@code PropertyChangeListener} on a specific property.
   *
   * @param propertyName
   *          The listened property.
   * @param listener
   *          The removed listener.
   * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(String,
   *      PropertyChangeListener)
   */
  void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener);

  /**
   * Gets listeners attached.
   * @return all of the {@code PropertyChangeListeners} added or an empty
   *         array if no listeners have been added
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  PropertyChangeListener[] getPropertyChangeListeners();

  /**
   * Gets listeners attached to a given property.
   * @param propertyName
   *          propertyName
   * @return all of the {@code PropertyChangeListeners} associated with the
   *         named property. If no such listeners have been added, or if
   *         {@code propertyName} is null, an empty array is returned.
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(String)
   */
  PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

  /**
   * Gets whether this property has at least a property change listener attached
   * to it.
   *
   * @param propertyName
   *          The listened property.
   * @return {@code true} if there is a property change listener attached
   *         to this property.
   */
  boolean hasListeners(String propertyName);

  /**
   * Notifies its {@code PropertyChangeListener}s on a specific property
   * change.
   *
   * @param property
   *     The property which changed.
   * @param oldValue
   *     The old value of the property.
   * @param newValue
   *     The new value of the property or {@code UNKNOWN}.
   */
  void firePropertyChange(String property, Object oldValue, Object newValue);

  /**
   * Delays events propagation by buffering them. When events are unblocked,
   * they get fired in the order they were recorded.
   *
   * @return true if the method call actually blocked the events. False if it
   *         was already blocked before.
   */
  boolean blockEvents();

  /**
   * Unblocks event propagation. All events that were buffered are fired.
   */
  void releaseEvents();
}
