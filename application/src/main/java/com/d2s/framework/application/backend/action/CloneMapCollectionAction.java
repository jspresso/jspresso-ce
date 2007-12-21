/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.util.collection.ObjectEqualityMap;

/**
 * An action used duplicate a collection of map domain objects. Cloning a map
 * should result in adding it to the collection the action was triggered on.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CloneMapCollectionAction extends AddMapToMasterAction {

  /**
   * Clones a map.
   * 
   * @param element
   *            the map to clone.
   * @param context
   *            the action context.
   * @return the cloned map.
   */
  protected Object cloneElement(Object element, @SuppressWarnings("unused")
  Map<String, Object> context) {
    return new ObjectEqualityMap<Object, Object>((Map<?, ?>) element);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<?> getAddedComponents(Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return null;
    }
    List<Object> elementClones = new ArrayList<Object>();
    for (int i = 0; i < selectedIndices.length; i++) {
      Object element = collectionConnector
          .getChildConnector(selectedIndices[i]).getConnectorValue();
      elementClones.add(cloneElement(element, context));
    }
    return elementClones;
  }
}
