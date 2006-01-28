/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.projection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.application.backend.projection.BeanProjection;
import com.d2s.framework.application.backend.projection.ChildProjection;
import com.d2s.framework.application.backend.projection.Projection;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This action removes the selected objects from the projected collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveFromProjectedCollectionAction extends
    AbstractCollectionAction {

  /**
   * Removes the selected objects from the projected collection.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    int[] selectedIndices = getSelectedIndices();
    ICollectionConnector collectionConnector = getModelConnector();

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return null;
    }

    ICompositeValueConnector projectionConnector = getProjectionConnector();
    BeanProjection projection = (BeanProjection) projectionConnector
        .getConnectorValue();

    Collection<IPropertyChangeCapable> projectedCollection;
    if (projection.getProjectedObjects() == null) {
      projectedCollection = new ArrayList<IPropertyChangeCapable>();
    } else {
      projectedCollection = new ArrayList<IPropertyChangeCapable>(projection
          .getProjectedObjects());
    }
    for (int i = 0; i < selectedIndices.length; i++) {
      Object removedObject = collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue();
      projectedCollection.remove(removedObject);
      removeAsChildProjection(projection, removedObject);
    }
    projection.setProjectedObjects(projectedCollection);
    return null;
  }

  private static void removeAsChildProjection(Projection parentProjection,
      Object removedObject) {
    if (parentProjection.getChildren() != null) {
      for (ChildProjection childProjection : new ArrayList<ChildProjection>(
          parentProjection.getChildren())) {
        if (childProjection instanceof BeanProjection
            && removedObject.equals(((BeanProjection) childProjection)
                .getProjectedObject())) {
          parentProjection.removeChild(childProjection);
        }
      }
    }
  }
}
