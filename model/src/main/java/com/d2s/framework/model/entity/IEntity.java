/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.io.Serializable;
import java.util.Map;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This interface must be implemented by all persistent entities in the
 * application domain. It establishes the minimal contract of an entity which is
 * providing id accessors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntity extends IEntityLifecycle, IPropertyChangeCapable,
    Cloneable {

  /**
   * constant for identifier property <code>ID</code>.
   */
  String ID      = "id";

  /**
   * constant for version property <code>VERSION</code>.
   */
  String VERSION = "version";

  /**
   * Gets the id used to uniquely identify an entity (surrogate key). The id is
   * assigned to the entity instance as soon as the entity is created in memory
   * an is afterwards made immutable so that <code>equals()</code> and
   * <code>hashCode()</code> can safely rely on it whenever they are transient
   * or not. It also establishes the minimal contract of a versionable entity
   * which is providing version accessors to handle access concurrency.
   * 
   * @return The id of the entity.
   */
  Serializable getId();

  /**
   * Gets the version of this entity.
   * 
   * @return the entity version.
   */
  Integer getVersion();

  /**
   * Notifies its <code>PropertyChangeListener</code>s on a specific property
   * change.
   * 
   * @param property
   *          The property which changed.
   * @param oldValue
   *          The old value of the property.
   * @param newValue
   *          The new value of the property or <code>UNKNOWN</code>.
   */
  void firePropertyChange(String property, Object oldValue, Object newValue);

  /**
   * Gets the interface or class establishing the entity contract.
   * 
   * @return the entity contract.
   */
  Class getContract();

  /**
   * This method is used to update a persistent property without triggering any
   * other behaviour except a <code>PropertyChangeEvent</code>.
   * 
   * @param propertyName
   *          the name of the property to set.
   * @param backendPropertyValue
   *          the value to set the property with.
   */
  void straightSetProperty(String propertyName, Object backendPropertyValue);

  /**
   * This method is used to update a persistent properties without triggering
   * any other behaviour except a <code>PropertyChangeEvent</code>.
   * 
   * @param properties
   *          the properties to set.
   */
  void straightSetProperties(Map<String, Object> properties);

  /**
   * This method is used to get a persistent property without triggering any
   * other behaviour.
   * 
   * @param propertyName
   *          the name of the property to get.
   * @return the current value of the property.
   */
  Object straightGetProperty(String propertyName);

  /**
   * This method is used to get all the persistent properties without triggering
   * any other behaviour.
   * 
   * @return the current properties values.
   */
  Map<String, Object> straightGetProperties();

  /**
   * Clones the entity potentially assigning it a new identifier and resetting
   * the version.
   * 
   * @param includeIdAndVersion
   *          if true, the identifier and version will be cloned also.
   * @return the cloned entity.
   */
  IEntity clone(boolean includeIdAndVersion);

  /**
   * Gets wether this entity has already been saved in the backing store.
   * 
   * @return true if the entity is not transient.
   */
  boolean isPersistent();

  /**
   * It is important to declare here so that ORM (hibernate for instance)
   * detects it has to delegate to the underlying instance when proxiing.
   * 
   * @return the hashcode.
   */
  int hashCode();

  /**
   * It is important to declare here so that ORM (hibernate for instance)
   * detects it has to delegate to the underlying instance when proxiing.
   * 
   * @param o
   *          the instance to compare to.
   * @return true if both instances are equal.
   */
  boolean equals(Object o);
}
