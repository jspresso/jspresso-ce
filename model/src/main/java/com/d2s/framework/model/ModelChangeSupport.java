/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.d2s.framework.util.lang.ObjectUtils;

/**
 * Helper class to ease the IModelChangeListener management.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
   *            The model provider to which this ModelChangeSupport is attached.
   *            sourceModelProvider will serve as <code>source</code> of fired
   *            ModelChangeEvent if no other is provided.
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
   *            The added listener.
   * @see IModelProvider#addModelChangeListener( IModelChangeListener)
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
   * Propagates the <code>ModelChangeEvent</code> as is (i.e. whithout
   * modifying its source) to the listeners.
   * 
   * @param evt
   *            the propagated <code>ModelChangeEvent</code>
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
   * Fires a new <code>ModelChangeEvent</code> built with <code>source</code>
   * as source and parameters as old and new values.
   * 
   * @param oldValue
   *            The old model provider's model value
   * @param newValue
   *            The new model provider's model value
   */
  public void fireModelChange(Object oldValue, Object newValue) {
    ModelChangeEvent evt = new ModelChangeEvent(source, oldValue, newValue);
    fireModelChange(evt);
  }

  /**
   * Removes a <code>IModelChangeListener</code>.
   * 
   * @param listener
   *            The removed listener.
   * @see IModelProvider#removeModelChangeListener( IModelChangeListener)
   */
  public synchronized void removeModelChangeListener(
      IModelChangeListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
    }
  }
}
