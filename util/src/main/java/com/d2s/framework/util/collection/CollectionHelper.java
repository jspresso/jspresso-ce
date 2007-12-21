/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.collection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * Helper class for collections management.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class CollectionHelper {

  private CollectionHelper() {
    // private constructor for helper class
  }

  /**
   * Clones a collection trying to invoke the <code>clone()</code> method
   * reflectively. If the latter is unsuccessful, returns an HashSet for a Set
   * and an ArrayList for a List.
   * 
   * @param <E>
   *            the type the collection element
   * @param collection
   *            the original collection
   * @return the cloned collection.
   */
  @SuppressWarnings("unchecked")
  public static <E> Collection<E> cloneCollection(Collection<E> collection) {
    if (collection == null) {
      return null;
    }
    Collection<E> clonedCollection = null;
    if (collection instanceof Cloneable) {
      // try to invoke the clone method reflectively
      try {
        clonedCollection = (Collection<E>) collection.getClass().getMethod(
            "clone", (Class[]) null).invoke(collection, (Object[]) null);
      } catch (IllegalArgumentException ex) {
        throw new NestedRuntimeException(ex);
      } catch (SecurityException ex) {
        throw new NestedRuntimeException(ex);
      } catch (IllegalAccessException ex) {
        throw new NestedRuntimeException(ex);
      } catch (InvocationTargetException ex) {
        throw new NestedRuntimeException(ex);
      } catch (NoSuchMethodException ex) {
        // Do nothing. the method simply does not exist.
      }
    }
    if (clonedCollection == null) {
      if (collection instanceof Set) {
        clonedCollection = new HashSet<E>(collection);
      } else if (collection instanceof List) {
        clonedCollection = new ArrayList<E>(collection);
      }
    }
    return clonedCollection;
  }
}
