/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.projection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.application.backend.projection.BeanProjection;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This action clones the selected objects in the projected collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CloneProjectedCollectionAction extends AbstractCollectionAction {

  /**
   * Clones the selected objects in the projected collection.
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
    Collection<IEntity> entityClones = new ArrayList<IEntity>();
    for (int i = 0; i < selectedIndices.length; i++) {
      entityClones.add(((IEntity) collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue()).clone(false));
    }
    projectedCollection.addAll(entityClones);
    projection.setProjectedObjects(projectedCollection);

    Map<String, Object> executionResult = new HashMap<String, Object>();
    executionResult.put(ActionContextConstants.SELECTED_INDICES,
        ConnectorHelper.getIndicesOf(collectionConnector, entityClones));
    return executionResult;
  }
}
