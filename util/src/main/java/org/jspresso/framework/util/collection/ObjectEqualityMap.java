/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.collection;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.bean.SinglePropertyChangeSupport;
import org.jspresso.framework.util.bean.SingleWeakPropertyChangeSupport;

/**
 * a map which equality is based on object identity.
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

  private static final long                         serialVersionUID = 8981204989863563244L;

  private transient SinglePropertyChangeSupport     propertyChangeSupport;
  private transient SingleWeakPropertyChangeSupport weakPropertyChangeSupport;

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   */
  public ObjectEqualityMap() {
    super();
    initPcSupports();
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   * 
   * @param initialCapacity
   *          initialCapacity.
   */
  public ObjectEqualityMap(int initialCapacity) {
    super(initialCapacity);
    initPcSupports();
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
    initPcSupports();
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   * 
   * @param m
   *          map.
   */
  public ObjectEqualityMap(Map<? extends K, ? extends V> m) {
    super(m);
    initPcSupports();
  }

  private void initPcSupports() {
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
    weakPropertyChangeSupport = new SingleWeakPropertyChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addWeakPropertyChangeListener(PropertyChangeListener listener) {
    weakPropertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addWeakPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    weakPropertyChangeSupport.addPropertyChangeListener(propertyName, listener);
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
   * Associates the specified value with the specified key in this map. If the
   * map previously contained a mapping for the key, the old value is replaced.
   * 
   * @param key
   *          key with which the specified value is to be associated
   * @param value
   *          value to be associated with the specified key
   * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
   *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
   *         can also indicate that the map previously associated <tt>null</tt>
   *         with <tt>key</tt>.)
   */
  @Override
  public V put(K key, V value) {
    V putVal = super.put(key, value);
    Object oldValue = putVal;
    if (oldValue instanceof Collection<?>) {
      oldValue = new ArrayList<Object>((Collection<?>) oldValue) {

        private static final long serialVersionUID = 7466229820747338355L;

        /**
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
      };
    }
    propertyChangeSupport.firePropertyChange(key.toString(), oldValue, value);
    weakPropertyChangeSupport.firePropertyChange(key.toString(), oldValue,
        value);
    return putVal;
  }

  /**
   * Removes the mapping for this key from this map if present.
   * 
   * @param key
   *          key whose mapping is to be removed from the map.
   * @return previous value associated with specified key, or <tt>null</tt> if
   *         there was no mapping for key. A <tt>null</tt> return can also
   *         indicate that the map previously associated <tt>null</tt> with the
   *         specified key.
   */
  @Override
  public V remove(Object key) {
    V oldValue = super.remove(key);
    propertyChangeSupport.firePropertyChange(key.toString(), oldValue, null);
    weakPropertyChangeSupport
        .firePropertyChange(key.toString(), oldValue, null);
    return oldValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
    weakPropertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    weakPropertyChangeSupport.removePropertyChangeListener(propertyName,
        listener);
  }

  /**
   * @param propertyName
   *          the property name.
   * @param oldValue
   *          the old value.
   * @param newValue
   *          the new value.
   * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String,
   *      java.lang.Object, java.lang.Object)
   */
  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    weakPropertyChangeSupport.firePropertyChange(propertyName, oldValue,
        newValue);
  }

  private void readObject(ObjectInputStream in) throws IOException,
      ClassNotFoundException {
    in.defaultReadObject();
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
    weakPropertyChangeSupport = new SingleWeakPropertyChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ObjectEqualityMap<K, V> clone() {
    @SuppressWarnings("unchecked")
    ObjectEqualityMap<K, V> clone = (ObjectEqualityMap<K, V>) super.clone();
    clone.propertyChangeSupport = new SinglePropertyChangeSupport(this);
    clone.weakPropertyChangeSupport = new SingleWeakPropertyChangeSupport(this);
    return clone;
  }
}
