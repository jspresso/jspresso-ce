/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.collection;

import java.util.HashMap;
import java.util.Map;

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
public class ObjectEqualityMap<K, V> extends HashMap<K, V> {

  private static final long serialVersionUID = 8981204989863563244L;

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   */
  public ObjectEqualityMap() {
    super();
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
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   *
   * @param initialCapacity
   *          initialCapacity.
   */
  public ObjectEqualityMap(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Constructs a new <code>ObjectEqualityMap</code> instance.
   *
   * @param m
   *          map.
   */
  public ObjectEqualityMap(Map<? extends K, ? extends V> m) {
    super(m);
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
}
