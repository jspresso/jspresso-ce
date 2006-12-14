/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.collection;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * a map which equality is based on object identity.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <K>
 *          the key class.
 * @param <V>
 *          the value class.
 */
public class ObjectEqualityMap<K, V> extends HashMap<K, V> implements
    IPropertyChangeCapable {

  private static final long     serialVersionUID = 8981204989863563244L;

  private PropertyChangeSupport propertyChangeSupport;

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   */
  public ObjectEqualityMap() {
    super();
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   *
   * @param initialCapacity
   *          initialCapacity.
   * @param loadFactor
   *          loadFactor.
   */
  public ObjectEqualityMap(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   *
   * @param initialCapacity
   *          initialCapacity.
   */
  public ObjectEqualityMap(int initialCapacity) {
    super(initialCapacity);
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   *
   * @param m
   *          map.
   */
  public ObjectEqualityMap(Map<? extends K, ? extends V> m) {
    super(m);
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * Solely based on object's equality.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    return this == o;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return super.hashCode();
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
   * {@inheritDoc}
   */
  @Override
  public V put(K key, V value) {
    V oldValue = get(key);
    V putVal = super.put(key, value);
    propertyChangeSupport.firePropertyChange(key.toString(), oldValue, value);
    return putVal;
  }
}
