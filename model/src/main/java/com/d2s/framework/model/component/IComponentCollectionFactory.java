/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component;

import java.util.Collection;

/**
 * This interface is implemented by factories of component collections.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the type of collection elements.
 */
public interface IComponentCollectionFactory<E> {

  /**
   * Given a collection interface (i.e. <code>Set</code>,<code>List</code>,
   * ...) this method creates a concrete implementation.
   * 
   * @param collectionInterface
   *            the interface which must be implemented by the created
   *            collection.
   * @return an empty instance of a concrete collection.
   */
  Collection<E> createEntityCollection(
      Class<?> collectionInterface);
}
