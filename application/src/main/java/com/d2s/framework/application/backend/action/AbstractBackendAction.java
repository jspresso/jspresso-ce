/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Map;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.bean.IBeanConnectorFactory;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.util.bean.IAccessorFactory;
import com.d2s.framework.view.action.AbstractAction;
import com.d2s.framework.view.action.ActionContextConstants;

/**
 * This class should serve as base class for implementing a action which
 * executes on the backend (domain model) of the application. It provides
 * accessors on components which are generally used through the action execution
 * process.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendAction extends AbstractAction {

  /**
   * {@inheritDoc}
   */
  public boolean isBackend() {
    return true;
  }

  /**
   * This is a utility method which is able to retrieve the model connector this
   * action has been executed on from its context. It uses well-known context
   * keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.MODEL_CONNECTOR</code> to get the model
   * value connector of the connector hierarchy.
   * </ul>
   * <p>
   * The returned connector mainly serves for retrieving the domain object the
   * action has to be triggered on.
   * 
   * @param context
   *          the action context.
   * @return the value connector this model action was triggered on.
   */
  public IValueConnector getModelConnector(Map<String, Object> context) {
    return (IValueConnector) context
        .get(ActionContextConstants.MODEL_CONNECTOR);
  }

  /**
   * This is a utility method which is able to retrieve the model descriptor
   * this action has been executed on from its context. It uses well-known
   * context keys of the action context which is :
   * <ul>
   * <li> <code>ActionContextConstants.MODEL_DESCRIPTOR</code>.
   * </ul>
   * 
   * @param context
   *          the action context.
   * @return the model descriptor this action executes on.
   */
  public IModelDescriptor getModelDescriptor(Map<String, Object> context) {
    return (IModelDescriptor) context
        .get(ActionContextConstants.MODEL_DESCRIPTOR);
  }

  /**
   * This is a utility method which is able to retrieve the module model
   * connector this action has been executed on from its context. It uses
   * well-known context keys of the action context which is :
   * <ul>
   * <li> <code>ActionContextConstants.MODULE_MODEL_CONNECTOR</code>.
   * </ul>
   * 
   * @param context
   *          the action context.
   * @return the module model connector this action executes on.
   */
  public ICompositeValueConnector getModuleConnector(Map<String, Object> context) {
    return (ICompositeValueConnector) context
        .get(ActionContextConstants.MODULE_MODEL_CONNECTOR);
  }

  /**
   * Gets the frontend controller out of the action context.
   * 
   * @param context
   *          the action context.
   * @return the frontend controller.
   */
  @SuppressWarnings("unchecked")
  protected IBackendController getController(Map<String, Object> context) {
    return (IBackendController) context.get(ActionContextConstants.BACK_CONTROLLER);
  }

  /**
   * Gets the accessorFactory.
   * 
   * @param context
   *          the action context.
   * @return the accessorFactory.
   */
  protected IAccessorFactory getAccessorFactory(Map<String, Object> context) {
    return getController(context).getAccessorFactory();
  }

  /**
   * Gets the entityFactory.
   * 
   * @param context
   *          the action context.
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory(Map<String, Object> context) {
    return getController(context).getEntityFactory();
  }

  /**
   * Gets the beanConnectorFactory.
   * 
   * @param context
   *          the action context.
   * @return the beanConnectorFactory.
   */
  protected IBeanConnectorFactory getBeanConnectorFactory(Map<String, Object> context) {
    return getController(context).getBeanConnectorFactory();
  }

}
