/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * Base implementation of a gate.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractGate implements IGate {

  private PropertyChangeSupport propertyChangeSupport;

  /**
   * Constructs a new <code>AbstractGate</code> instance.
   */
  public AbstractGate() {
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *          the name of the property.
   * @param oldValue
   *          the old property value.
   * @param newValue
   *          the new property value.
   */
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Directly delegates to propertyChangeSupport.
   * 
   * @param propertyName
   *          the name of the property.
   * @param oldValue
   *          the old property value.
   * @param newValue
   *          the new property value.
   */
  protected void firePropertyChange(String propertyName, boolean oldValue,
      boolean newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractGate clone() {
    try {
      AbstractGate clonedGate = (AbstractGate) super.clone();
      clonedGate.propertyChangeSupport = new PropertyChangeSupport(clonedGate);
      return clonedGate;
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
  }
}
