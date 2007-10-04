/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a helper class to ease collection connector management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @see com.d2s.framework.binding.model.ModelCollectionPropertyConnector
 * @see com.d2s.framework.binding.AbstractCollectionConnector
 * @version $LastChangedRevision$
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
   *            the base identifier to be used to compose the new connector id.
   * @param i
   *            the index used.
   * @return the created connector id.
   */
  public static String computeConnectorId(String baseId, int i) {
    return baseId + "[" + i + "]";
  }

  /**
   * Computes the array of element indices which where added to a collection.
   * 
   * @param smallCollection
   *            the original collection.
   * @param bigCollection
   *            the collection with added elements.
   * @return the the array of element indices which where added to tyhe original
   *         collection
   */
  public static int[] computeDifferenceIndices(Collection<?> smallCollection,
      Collection<?> bigCollection) {
    List<Integer> addedIndices = new ArrayList<Integer>();
    int index = 0;
    for (Iterator<?> ite = bigCollection.iterator(); ite.hasNext(); index++) {
      if (smallCollection == null || !smallCollection.contains(ite.next())) {
        if (smallCollection == null) {
          ite.next();
        }
        addedIndices.add(new Integer(index));
      }
    }
    int[] result = new int[addedIndices.size()];
    for (int i = 0; i < addedIndices.size(); i++) {
      result[i] = addedIndices.get(i).intValue();
    }
    return result;
  }
}
