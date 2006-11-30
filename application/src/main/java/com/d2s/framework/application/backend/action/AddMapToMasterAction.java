/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * An action used in master/detail views where models are backed by maps to
 * create and add a new detail to a master domain object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddMapToMasterAction extends AbstractAddCollectionToMasterAction {

  /**
   * Returns the map accessor factory.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IAccessorFactory getAccessorFactory(Map<String, Object> context) {
    return getController(context).getMapAccessorFactory();
  }

  /**
   * Gets the new map component to add.
   *
   * @param context
   *          the action context.
   * @return the map to add to the collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected List<?> getAddedComponents(@SuppressWarnings("unused")
  Map<String, Object> context) {
    return Collections.singletonList(new ObjectEqualityMap<String, Object>());
  }

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
  public static class ObjectEqualityMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 8981204989863563244L;

    /**
     * Constructs a new <code>ObjectEqualityMap</code> instance.
     */
    protected ObjectEqualityMap() {
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
    protected ObjectEqualityMap(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
    }

    /**
     * Constructs a new <code>ObjectEqualityMap</code> instance.
     *
     * @param initialCapacity
     *          initialCapacity.
     */
    protected ObjectEqualityMap(int initialCapacity) {
      super(initialCapacity);
    }

    /**
     * Constructs a new <code>ObjectEqualityMap</code> instance.
     *
     * @param m
     *          map.
     */
    protected ObjectEqualityMap(Map<? extends K, ? extends V> m) {
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
}
