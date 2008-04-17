/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Creates new empty components collections.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the type of collection elements.
 */
public class DefaultComponentCollectionFactory<E> implements
    IComponentCollectionFactory<E> {

  /**
   * Creates concrete empty collections based on standard java collections.
   * <p>
   * {@inheritDoc}
   */
  public Collection<E> createEntityCollection(Class<?> collectionInterface) {
    if (Set.class.isAssignableFrom(collectionInterface)) {
      return new LinkedHashSet<E>();
    } else if (List.class.isAssignableFrom(collectionInterface)) {
      return new ArrayList<E>();
    }
    throw new ComponentException("Collection type " + collectionInterface
        + " is not supported for components.");
  }

}
