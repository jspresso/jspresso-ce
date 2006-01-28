/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

import com.d2s.framework.util.IGate;
import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * This abstract class holds some default implementation for connector. All the
 * default connectors inherit from this default behaviour.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public abstract class AbstractConnector implements IConnector {

  private String                id;
  private PropertyChangeSupport propertyChangeSupport;

  /**
   * Constructs a new AbstractConnector using an identifier. In case of a bean
   * connector, this identifier must be the bean property the connector
   * connects.
   * 
   * @param id
   *          The connector identifier.
   */
  public AbstractConnector(String id) {
    this.id = id;
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  public String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  public AbstractConnector clone(String newConnectorId) {
    try {
      AbstractConnector clonedConnector = (AbstractConnector) super.clone();
      clonedConnector.id = newConnectorId;
      clonedConnector.propertyChangeSupport = new PropertyChangeSupport(
          clonedConnector);
      return clonedConnector;
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractConnector clone() {
    return clone(getId());
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
   * Returns true if and only if gates are null, empty or all open.
   * 
   * @param gates
   *          the gates collection.
   * @return gates status.
   */
  protected static boolean areGatesOpen(Collection<IGate> gates) {
    if (gates != null) {
      for (IGate gate : gates) {
        if (!gate.isOpen()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Default implementation does nothing.
   * <p>
   * {@inheritDoc}
   */
  public void updateState() {
    // Default empty implementation.
  }
}
