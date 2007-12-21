/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.component;

import java.util.Map;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This interface must be implemented by all model components in the application
 * domain. It establishes the minimal contract of a model component.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponent extends ILifecycleCapable, IPropertyChangeCapable {

  /**
   * Notifies its <code>PropertyChangeListener</code>s on a specific property
   * change.
   * 
   * @param property
   *            The property which changed.
   * @param oldValue
   *            The old value of the property.
   * @param newValue
   *            The new value of the property or <code>UNKNOWN</code>.
   */
  void firePropertyChange(String property, Object oldValue, Object newValue);

  /**
   * Gets the interface or class establishing the entity contract.
   * 
   * @return the entity contract.
   */
  Class<? extends IComponent> getContract();

  /**
   * This method is used to get all the persistent properties without triggering
   * any other behaviour.
   * 
   * @return the current properties values.
   */
  Map<String, Object> straightGetProperties();

  /**
   * This method is used to get a persistent property without triggering any
   * other behaviour.
   * 
   * @param propertyName
   *            the name of the property to get.
   * @return the current value of the property.
   */
  Object straightGetProperty(String propertyName);

  /**
   * This method is used to update a persistent properties without triggering
   * any other behaviour except a <code>PropertyChangeEvent</code>.
   * 
   * @param properties
   *            the properties to set.
   */
  void straightSetProperties(Map<String, Object> properties);

  /**
   * This method is used to update a persistent property without triggering any
   * other behaviour except a <code>PropertyChangeEvent</code>.
   * 
   * @param propertyName
   *            the name of the property to set.
   * @param backendPropertyValue
   *            the value to set the property with.
   */
  void straightSetProperty(String propertyName, Object backendPropertyValue);
}
