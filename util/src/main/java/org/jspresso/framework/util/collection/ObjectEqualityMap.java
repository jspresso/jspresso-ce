/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.collection;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.bean.SinglePropertyChangeSupport;

/**
 * a map which equality is based on object identity.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

  private static final long               serialVersionUID = 8981204989863563244L;

  private transient PropertyChangeSupport propertyChangeSupport;

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   */
  public ObjectEqualityMap() {
    super();
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   * 
   * @param initialCapacity
   *          initialCapacity.
   */
  public ObjectEqualityMap(int initialCapacity) {
    super(initialCapacity);
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
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
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   * 
   * @param m
   *          map.
   */
  public ObjectEqualityMap(Map<? extends K, ? extends V> m) {
    super(m);
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
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
  @Override
  public V put(K key, V value) {
    V putVal = super.put(key, value);
    Object oldValue = putVal;
    if (oldValue instanceof Collection) {
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
    return putVal;
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
  }

  private void readObject(ObjectInputStream in) throws IOException,
      ClassNotFoundException {
    in.defaultReadObject();
    propertyChangeSupport = new SinglePropertyChangeSupport(this);
  }
}
