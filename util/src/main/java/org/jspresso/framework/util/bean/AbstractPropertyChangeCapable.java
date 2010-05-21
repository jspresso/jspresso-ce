/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

  private PropertyChangeSupport propertyChangeSupport;

  /**
   * Constructs a new <code>AbstractPropertyChangeCapable</code> instance.
   */
  public AbstractPropertyChangeCapable() {
    this.propertyChangeSupport = new SinglePropertyChangeSupport(this);
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
  @Override
  public AbstractPropertyChangeCapable clone() {
    try {
      AbstractPropertyChangeCapable clonedBean = (AbstractPropertyChangeCapable) super
          .clone();
      clonedBean.propertyChangeSupport = new SinglePropertyChangeSupport(
          clonedBean);
      return clonedBean;
    } catch (CloneNotSupportedException ex) {
      throw new NestedRuntimeException(ex);
    }
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
   * @param evt
   *          evt
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.beans.PropertyChangeEvent)
   */
  protected void firePropertyChange(PropertyChangeEvent evt) {
    propertyChangeSupport.firePropertyChange(evt);
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
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
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
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
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
    propertyChangeSupport.firePropertyChange(propertyName, new Long(oldValue),
        new Long(newValue));
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
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * @return all of the <code>PropertyChangeListeners</code> added or an empty
   *         array if no listeners have been added
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
   */
  protected PropertyChangeListener[] getPropertyChangeListeners() {
    return propertyChangeSupport.getPropertyChangeListeners();
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
    return propertyChangeSupport.getPropertyChangeListeners(propertyName);
  }

  /**
   * @param propertyName
   *          propertyName
   * @return true if there are one or more listeners for the given property.
   * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
   */
  protected boolean hasListeners(String propertyName) {
    return propertyChangeSupport.hasListeners(propertyName);
  }
}
