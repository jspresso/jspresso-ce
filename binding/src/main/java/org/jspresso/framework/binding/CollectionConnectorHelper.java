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
package org.jspresso.framework.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a helper class to ease collection connector management.
 *
 * @see org.jspresso.framework.binding.model.ModelCollectionPropertyConnector
 * @see org.jspresso.framework.binding.AbstractCollectionConnector
 * @author Vincent Vandenschrick
 */
public final class CollectionConnectorHelper {

  /**
   * Constructs a new CollectionConnectorHelper instance.
   */
  private CollectionConnectorHelper() {
    super();
  }

  /**
   * Builds a connector id from a base id and an index. This serves as child
   * connector ids in collection connectors. As of now it returns "baseId[i]"
   *
   * @param baseId
   *          the base identifier to be used to compose the new connector id.
   * @param i
   *          the index used.
   * @return the created connector id.
   */
  public static String computeStorageKey(String baseId, int i) {
    return Integer.toString(i);
  }

  /**
   * Computes the array of element indices which where added to a collection.
   *
   * @param smallCollection
   *          the original collection.
   * @param bigCollection
   *          the collection with added elements.
   * @return the the array of element indices which where added to the original
   *         collection
   */
  public static int[] computeDifferenceIndices(Collection<?> smallCollection,
      Collection<?> bigCollection) {
    List<Integer> addedIndices = new ArrayList<>();
    int index = 0;
    for (Iterator<?> ite = bigCollection.iterator(); ite.hasNext(); index++) {
      if (smallCollection == null || !smallCollection.contains(ite.next())) {
        if (smallCollection == null) {
          ite.next();
        }
        addedIndices.add(index);
      }
    }
    int[] result = new int[addedIndices.size()];
    for (int i = 0; i < addedIndices.size(); i++) {
      result[i] = addedIndices.get(i);
    }
    return result;
  }

  /**
   * Extracts the main collection connector from a connector hierarchy.
   *
   * @param rootConnector
   *          the root connector.
   * @return the the main collection connector.
   */
  public static ICollectionConnector extractMainCollectionConnector(
      IValueConnector rootConnector) {
    if (rootConnector instanceof ICollectionConnector) {
      return (ICollectionConnector) rootConnector;
    }
    if (rootConnector instanceof ICompositeValueConnector) {
      for (String childName : ((ICompositeValueConnector) rootConnector)
          .getChildConnectorKeys()) {
        IValueConnector childCollectionConnector = extractMainCollectionConnector(((ICompositeValueConnector) rootConnector)
            .getChildConnector(childName));
        if (childCollectionConnector != null) {
          return (ICollectionConnector) childCollectionConnector;
        }
      }
    }
    return null;
  }
}
