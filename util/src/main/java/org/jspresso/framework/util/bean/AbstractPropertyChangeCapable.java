/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * Abstract class to build a property change capable bean.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @internal
 */
public abstract class AbstractPropertyChangeCapable implements
    IPropertyChangeCapable, Cloneable {

  private transient SinglePropertyChangeSupport propertyChangeSupport;

  private synchronized void initializePropertyChangeSupportIfNeeded() {
    if (propertyChangeSupport == null) {
      this.propertyChangeSupport = new SinglePropertyChangeSupport(this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    initializePropertyChangeSupportIfNeeded();
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    initializePropertyChangeSupportIfNeeded();
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractPropertyChangeCapable clone() {
    try {
      AbstractPropertyChangeCapable clonedBean = (AbstractPropertyChangeCapable) super
          .clone();
      clonedBean.propertyChangeSupport = null;
      return clonedBean;
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport.removePropertyChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport
          .removePropertyChangeListener(propertyName, listener);
    }
  }

  /**
   * @param evt
   *          evt
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)
   */
  protected void firePropertyChange(PropertyChangeEvent evt) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport.firePropertyChange(evt);
    }
  }

  /**
   * @param propertyName
   *          propertyName
   * @param oldValue
   *          oldValue
   * @param newValue
   *          newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String,
   *      boolean, boolean)
   */
  protected void firePropertyChange(String propertyName, boolean oldValue,
      boolean newValue) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport
          .firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  /**
   * @param propertyName
   *          propertyName
   * @param oldValue
   *          oldValue
   * @param newValue
   *          newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String,
   *      int, int)
   */
  protected void firePropertyChange(String propertyName, int oldValue,
      int newValue) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport
          .firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  /**
   * @param propertyName
   *          propertyName
   * @param oldValue
   *          oldValue
   * @param newValue
   *          newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String,
   *      int, int)
   */
  protected void firePropertyChange(String propertyName, long oldValue,
      long newValue) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport.firePropertyChange(propertyName,
          new Long(oldValue), new Long(newValue));
    }
  }

  /**
   * @param propertyName
   *          propertyName
   * @param oldValue
   *          oldValue
   * @param newValue
   *          newValue
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String,
   *      java.lang.Object, java.lang.Object)
   */
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    if (propertyChangeSupport != null) {
      propertyChangeSupport
          .firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  /**
   * @return all of the <code>PropertyChangeListeners</code> added or an empty
   *         array if no listeners have been added
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  protected PropertyChangeListener[] getPropertyChangeListeners() {
    if (propertyChangeSupport != null) {
      return propertyChangeSupport.getPropertyChangeListeners();
    }
    return new PropertyChangeListener[0];
  }

  /**
   * @param propertyName
   *          propertyName
   * @return all of the <code>PropertyChangeListeners</code> associated with the
   *         named property. If no such listeners have been added, or if
   *         <code>propertyName</code> is null, an empty array is returned.
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
   */
  protected PropertyChangeListener[] getPropertyChangeListeners(
      String propertyName) {
    if (propertyChangeSupport != null) {
      return propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }
    return new PropertyChangeListener[0];
  }

  /**
   * @param propertyName
   *          propertyName
   * @return true if there are one or more listeners for the given property.
   * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
   */
  protected boolean hasListeners(String propertyName) {
    if (propertyChangeSupport != null) {
      return propertyChangeSupport.hasListeners(propertyName);
    }
    return false;
  }
}
