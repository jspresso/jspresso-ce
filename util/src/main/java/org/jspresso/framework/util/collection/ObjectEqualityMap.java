/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * A map which equality is based on object identity.
 *
 * @author Vincent Vandenschrick
 * @param <K>
 *          the key class.
 * @param <V>
 *          the value class.
 */
@SuppressWarnings("NullableProblems")
public class ObjectEqualityMap<K, V> extends AbstractPropertyChangeCapable
    implements Map<K, V>, Serializable {

  private static final long serialVersionUID = 6871606396821418100L;
  private HashMap<K, V> delegate;

  /**
   * Constructs a new {@code ObjectEqualityMap} instance.
   */
  public ObjectEqualityMap() {
    delegate = new HashMap<>();
  }

  /**
   * Constructs a new {@code ObjectEqualityMap} instance.
   *
   * @param initialCapacity
   *          initialCapacity.
   */
  public ObjectEqualityMap(int initialCapacity) {
    delegate = new HashMap<>(initialCapacity);
  }

  /**
   * Constructs a new {@code ObjectEqualityMap} instance.
   *
   * @param initialCapacity
   *          initialCapacity.
   * @param loadFactor
   *          loadFactor.
   */
  public ObjectEqualityMap(int initialCapacity, float loadFactor) {
    delegate = new HashMap<>(initialCapacity, loadFactor);
  }

  /**
   * Constructs a new {@code ObjectEqualityMap} instance.
   *
   * @param m
   *          map.
   */
  public ObjectEqualityMap(Map<? extends K, ? extends V> m) {
    delegate = new HashMap<>(m);
  }

  /**
   * Fires a property change on put.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public V put(K key, V value) {
    V putVal = delegate.put(key, value);
    Object oldValue = putVal;
    if (oldValue instanceof Collection<?>) {
      oldValue = new ArrayList<Object>((Collection<?>) oldValue) {

        private static final long serialVersionUID = 7466229820747338355L;

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object o) {
          return this == o;
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("EmptyMethod")
        @Override
        public int hashCode() {
          return super.hashCode();
        }
      };
    }
    firePropertyChange(key.toString(), oldValue, value);
    return putVal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public V remove(Object key) {
    V oldValue = delegate.remove(key);
    firePropertyChange(key.toString(), oldValue, null);
    return oldValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int size() {
    return delegate.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean containsKey(Object key) {
    return delegate.containsKey(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean containsValue(Object value) {
    return delegate.containsValue(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public V get(Object key) {
    return delegate.get(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    delegate.putAll(m);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    delegate.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<K> keySet() {
    return delegate.keySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<V> values() {
    return delegate.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<java.util.Map.Entry<K, V>> entrySet() {
    return delegate.entrySet();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public ObjectEqualityMap<K, V> clone() {
    ObjectEqualityMap<K, V> clone = (ObjectEqualityMap<K, V>) super.clone();
    clone.delegate = (HashMap<K, V>) delegate.clone();
    return clone;
  }
}
