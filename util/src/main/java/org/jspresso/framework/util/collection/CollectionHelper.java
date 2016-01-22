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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * Helper class for collections management.
 *
 * @author Vincent Vandenschrick
 */
public final class CollectionHelper {

  private CollectionHelper() {
    // private constructor for helper class
  }

  /**
   * Clones a collection trying to invoke the {@code clone()} method
   * reflectively. If the latter is unsuccessful, returns an HashSet for a Set
   * and an ArrayList for a List.
   *
   * @param <E>
   *          the type the collection element
   * @param collection
   *          the original collection
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
        clonedCollection = (Collection<E>) collection.getClass()
            .getMethod("clone", (Class<?>[]) null)
            .invoke(collection, (Object[]) null);
      } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException | SecurityException ex) {
        throw new NestedRuntimeException(ex);
      } catch (NoSuchMethodException ex) {
        // Do nothing. the method simply does not exist.
      }
    }
    if (clonedCollection == null) {
      if (collection instanceof Set) {
        clonedCollection = new HashSet<>(collection);
      } else if (collection instanceof List) {
        clonedCollection = new ArrayList<>(collection);
      }
    }
    return clonedCollection;
  }
}
