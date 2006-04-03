/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collection;

import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.IPropertyChangeCapable;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.module.BeanModule;

/**
 * This action clones the selected objects in the projected collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CloneModuleObjectsAction extends AbstractCollectionAction {

  /**
   * Clones the selected objects in the projected collection.
   * <p>
   * {@inheritDoc}
   */
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    int[] selectedIndices = getSelectedIndices();
    ICollectionConnector collectionConnector = getModelConnector();

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return;
    }

    ICompositeValueConnector moduleConnector = getModuleConnector();
    BeanModule module = (BeanModule) moduleConnector.getConnectorValue();

    Collection<IPropertyChangeCapable> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<IPropertyChangeCapable>();
    } else {
      projectedCollection = new ArrayList<IPropertyChangeCapable>(module
          .getModuleObjects());
    }
    Collection<IEntity> entityClones = new ArrayList<IEntity>();
    for (int i = 0; i < selectedIndices.length; i++) {
      entityClones.add(((IEntity) collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue()).clone(false));
    }
    projectedCollection.addAll(entityClones);
    module.setModuleObjects(projectedCollection);

    getModelConnector().setConnectorValue(projectedCollection);

    getContext().put(ActionContextConstants.SELECTED_INDICES,
        ConnectorHelper.getIndicesOf(collectionConnector, entityClones));
  }
}
