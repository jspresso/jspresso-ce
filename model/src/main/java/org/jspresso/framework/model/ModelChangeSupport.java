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
package org.jspresso.framework.model;

import java.util.Set;

import gnu.trove.set.hash.TLinkedHashSet;

/**
 * Helper class to ease the IModelChangeListener management.
 *
 * @author Vincent Vandenschrick
 */
public class ModelChangeSupport {

  private Set<IModelChangeListener> listeners;

  private IModelProvider            source;

  /**
   * Constructs a new Connector change support.
   *
   * @param sourceModelProvider
   *          The model provider to which this ModelChangeSupport is attached.
   *          sourceModelProvider will serve as {@code source} of fired
   *          ModelChangeEvent if no other is provided.
   */
  public ModelChangeSupport(IModelProvider sourceModelProvider) {
    if (sourceModelProvider == null) {
      throw new NullPointerException();
    }
    source = sourceModelProvider;
  }

  /**
   * Adds a new {@code IModelChangeListener}.
   *
   * @param listener
   *          The added listener.
   * @see IModelProvider#addModelChangeListener(IModelChangeListener)
   */
  public synchronized void addModelChangeListener(IModelChangeListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new TLinkedHashSet<>();
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Propagates the {@code ModelChangeEvent} as is (i.e. without modifying
   * its source) to the listeners.
   *
   * @param evt
   *          the propagated {@code ModelChangeEvent}
   */
  public void fireModelChange(ModelChangeEvent evt) {
    if (listeners != null) {
      Object oldValue = evt.getOldValue();
      Object newValue = evt.getNewValue();
      // Do not use equals, since a model change event be propagated
      // for 2 different instances even if they are equal.
      // see bug #1017
      // if (ObjectUtils.equals(oldValue, newValue)) {
      // return;
      // }
      if (oldValue == newValue) {
        return;
      }
      for (IModelChangeListener listener : listeners.toArray(new IModelChangeListener[listeners.size()])) {
        listener.modelChange(evt);
      }
    }
  }

  /**
   * Fires a new {@code ModelChangeEvent} built with {@code source} as
   * source and parameters as old and new values.
   *
   * @param oldValue
   *          The old model provider's model value
   * @param newValue
   *          The new model provider's model value
   */
  public void fireModelChange(Object oldValue, Object newValue) {
    ModelChangeEvent evt = new ModelChangeEvent(source, oldValue, newValue);
    fireModelChange(evt);
  }

  /**
   * Removes a {@code IModelChangeListener}.
   *
   * @param listener
   *          The removed listener.
   * @see IModelProvider#removeModelChangeListener(IModelChangeListener)
   */
  public synchronized void removeModelChangeListener(
      IModelChangeListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
    }
  }
}
