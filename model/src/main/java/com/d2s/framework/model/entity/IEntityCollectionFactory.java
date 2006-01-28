/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.util.Collection;

/**
 * This interface is implemented by factories of entity collections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityCollectionFactory {

  /**
   * Given a collection interface (i.e. <code>Set</code>,<code>List</code>,
   * ...) this method creates a concrete implementation.
   * 
   * @param collectionInterface
   *          the interface which must be implemented by the created collection.
   * @return an empty instance of a concrete collection.
   */
  Collection<IEntity> createEntityCollection(Class collectionInterface);
}
