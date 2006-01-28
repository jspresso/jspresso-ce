/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

/**
 * Helper class to ease the IBeanChangeListener management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanChangeSupport {

  private IBeanProvider            source;

  private Set<IBeanChangeListener> listeners;

  /**
   * Constructs a new Connector change support.
   * 
   * @param sourceBeanProvider
   *          The bean provider to which this BeanChangeSupport is attached.
   *          sourceBeanProvider will serve as <code>source</code> of fired
   *          BeanChangeEvent if no other is provided.
   */
  public BeanChangeSupport(IBeanProvider sourceBeanProvider) {
    if (sourceBeanProvider == null) {
      throw new NullPointerException();
    }
    source = sourceBeanProvider;
  }

  /**
   * Adds a new <code>IBeanChangeListener</code>.
   * 
   * @param listener
   *          The added listener.
   * @see IBeanProvider#addBeanChangeListener( IBeanChangeListener)
   */
  public synchronized void addBeanChangeListener(IBeanChangeListener listener) {
    if (listener != null) {
      if (listeners == null) {
        listeners = new LinkedHashSet<IBeanChangeListener>();
      }
      if (!listeners.contains(listener)) {
        listeners.add(listener);
      }
    }
  }

  /**
   * Removes a <code>IBeanChangeListener</code>.
   * 
   * @param listener
   *          The removed listener.
   * @see IBeanProvider#removeBeanChangeListener( IBeanChangeListener)
   */
  public synchronized void removeBeanChangeListener(IBeanChangeListener listener) {
    if (listener != null && listeners != null) {
      listeners.remove(listener);
    }
  }

  /**
   * Fires a new <code>BeanChangeEvent</code> built with <code>source</code>
   * as source and parameters as old and new values.
   * 
   * @param oldValue
   *          The old bean provider's bean value
   * @param newValue
   *          The new bean provider's bean value
   */
  public void fireBeanChange(IPropertyChangeCapable oldValue,
      IPropertyChangeCapable newValue) {
    BeanChangeEvent evt = new BeanChangeEvent(source, oldValue, newValue);
    fireBeanChange(evt);
  }

  /**
   * Propagates the <code>BeanChangeEvent</code> as is (i.e. whithout
   * modifying its source) to the listeners.
   * 
   * @param evt
   *          the propagated <code>BeanChangeEvent</code>
   */
  public void fireBeanChange(BeanChangeEvent evt) {
    if (listeners != null) {
      Object oldValue = evt.getOldValue();
      Object newValue = evt.getNewValue();
      if (ObjectUtils.equals(oldValue, newValue)) {
        return;
      }
      for (IBeanChangeListener listener : listeners) {
        listener.beanChange(evt);
      }
    }
  }
}
