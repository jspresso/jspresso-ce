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
package org.jspresso.framework.model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * Helper class to ease the IModelChangeListener management.
 * 
 * @version $LastChangedRevision$
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
   *          sourceModelProvider will serve as <code>source</code> of fired
   *          ModelChangeEvent if no other is provided.
   */
  public ModelChangeSupport(IModelProvider sourceModelProvider) {
    if (sourceModelProvider == null) {
      throw new NullPointerException();
    }
    source = sourceModelProvider;
  }

  /**
   * Adds a new <code>IModelChangeListener</code>.
   * 
   * @param listener
   *          The added listener.
   * @see IModelProvider#addModelChangeListener(IModelChangeListener)
   */
  public synchronized void addModelChangeListener(IModelChangeListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new LinkedHashSet<IModelChangeListener>();
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Propagates the <code>ModelChangeEvent</code> as is (i.e. whithout modifying
   * its source) to the listeners.
   * 
   * @param evt
   *          the propagated <code>ModelChangeEvent</code>
   */
  public void fireModelChange(ModelChangeEvent evt) {
    if (listeners != null) {
      Object oldValue = evt.getOldValue();
      Object newValue = evt.getNewValue();
      if (ObjectUtils.equals(oldValue, newValue)) {
        return;
      }
      for (IModelChangeListener listener : listeners) {
        listener.modelChange(evt);
      }
    }
  }

  /**
   * Fires a new <code>ModelChangeEvent</code> built with <code>source</code> as
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
   * Removes a <code>IModelChangeListener</code>.
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
