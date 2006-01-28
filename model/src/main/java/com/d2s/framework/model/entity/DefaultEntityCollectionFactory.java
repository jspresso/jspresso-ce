/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Creates new empty entity collections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultEntityCollectionFactory implements IEntityCollectionFactory {

  /**
   * Creates concrete empty collections based on standard java collections.
   * <p>
   * {@inheritDoc}
   */
  public Collection<IEntity> createEntityCollection(Class collectionInterface) {
    if (Set.class.isAssignableFrom(collectionInterface)) {
      return new LinkedHashSet<IEntity>();
    } else if (List.class.isAssignableFrom(collectionInterface)) {
      return new ArrayList<IEntity>();
    }
    throw new EntityException("Collection type " + collectionInterface
        + " is not supported for entities.");
  }

}
