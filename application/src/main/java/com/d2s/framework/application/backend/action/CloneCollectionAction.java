/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;

/**
 * An action used duplicate a collection of domain objects. Cloning an entity
 * should result in adding it to the collection the action was triggered on.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CloneCollectionAction extends AbstractCollectionAction {

  /**
   * Retrieves the managed collection from the model connector then clones the
   * selected elements.
   * <p>
   * {@inheritDoc}
   */
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return;
    }
    Collection<IEntity> entityClones = new ArrayList<IEntity>();
    for (int i = 0; i < selectedIndices.length; i++) {
      entityClones.add(((IEntity) collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue()).clone(getEntityFactory(context), false));
    }
    context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
        .getIndicesOf(collectionConnector, entityClones));
  }

}
