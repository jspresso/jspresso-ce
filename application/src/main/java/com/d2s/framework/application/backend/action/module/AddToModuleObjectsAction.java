/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;

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
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    BeanCollectionModule module = (BeanCollectionModule) moduleConnector
        .getConnectorValue();

    Collection<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<Object>();
    } else {
      projectedCollection = new ArrayList<Object>(module.getModuleObjects());
    }

    Object newModuleObject = createNewModuleObject(actionHandler, context);
    projectedCollection.add(newModuleObject);
    module.setModuleObjects(projectedCollection);

    ICollectionConnector moduleObjectsConnector = getSourceModelConnector(context);
    if (moduleObjectsConnector == null) {
      moduleObjectsConnector = getModelConnector(context);
    }

    moduleObjectsConnector.setConnectorValue(projectedCollection);

    context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
        .getIndicesOf(moduleObjectsConnector, Collections
            .singleton(newModuleObject)));
    return super.execute(actionHandler, context);
  }

  /**
   * Creates a new entity to add to the projected object collection.
   * 
   * @param actionHandler
   *          the action handler (generally the controller).
   * @param context
   *          the action context.
   * @return the created entity.
   */
  @SuppressWarnings("unchecked")
  protected Object createNewModuleObject(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    IComponentDescriptor projectedComponentDescriptor = ((ICollectionDescriptor) getModelDescriptor(context))
        .getElementDescriptor();

    return getEntityFactory(context).createEntityInstance(
        (Class<? extends IEntity>) projectedComponentDescriptor
            .getComponentContract());
  }
}
