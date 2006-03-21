/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.IPropertyChangeCapable;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.module.BeanModule;

/**
 * This action adds a new object in the projected collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddToModuleObjectsAction extends AbstractCollectionAction {

  /**
   * Adds a new object in the projected collection.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    ICompositeValueConnector moduleConnector = getModuleConnector();
    BeanModule module = (BeanModule) moduleConnector.getConnectorValue();
    IComponentDescriptor projectedComponentDescriptor = ((ICollectionDescriptor) getModelDescriptor())
        .getElementDescriptor();

    Collection<IPropertyChangeCapable> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<IPropertyChangeCapable>();
    } else {
      projectedCollection = new ArrayList<IPropertyChangeCapable>(module
          .getModuleObjects());
    }
    IEntity newEntity = getEntityFactory().createEntityInstance(
        projectedComponentDescriptor.getComponentContract());
    projectedCollection.add(newEntity);
    module.setModuleObjects(projectedCollection);

    getModelConnector().setConnectorValue(projectedCollection);

    Map<String, Object> executionResult = new HashMap<String, Object>();
    executionResult.put(ActionContextConstants.SELECTED_INDICES,
        ConnectorHelper.getIndicesOf(getModelConnector(), Collections
            .singleton(newEntity)));
    return executionResult;
  }
}
