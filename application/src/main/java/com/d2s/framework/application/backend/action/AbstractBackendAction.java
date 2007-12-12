/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.action.AbstractAction;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * This class should serve as base class for implementing a action which
 * executes on the backend (domain model) of the application. It provides
 * accessors on components which are generally used through the action execution
 * process.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendAction extends AbstractAction {

  private AbstractBackendAction nextAction;

  /**
   * Executes the next action.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (getNextAction() != null) {
      return actionHandler.execute(getNextAction(), context);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getLocale(Map<String, Object> context) {
    return getController(context).getLocale();
  }

  /**
   * This is a utility method which is able to retrieve the model connector this
   * action has been executed on from its context. It uses well-known context
   * keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.VIEW_CONNECTOR</code> to get the model
   * value connector of the connector hierarchy.
   * </ul>
   * <p>
   * The returned connector mainly serves for retrieving the domain object the
   * action has to be triggered on.
   * 
   * @param context
   *            the action context.
   * @return the value connector this model action was triggered on.
   */
  public IValueConnector getModelConnector(Map<String, Object> context) {
    return ((IValueConnector) context
        .get(ActionContextConstants.VIEW_CONNECTOR)).getModelConnector();
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
   *            the action context.
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
   *            the action context.
   * @return the module model connector this action executes on.
   */
  public ICompositeValueConnector getModuleConnector(Map<String, Object> context) {
    return (ICompositeValueConnector) ((ICompositeValueConnector) context
        .get(ActionContextConstants.MODULE_VIEW_CONNECTOR)).getModelConnector();
  }

  /**
   * This is a utility method which is able to retrieve the source model
   * connector this action has been executed on from its context. It uses
   * well-known context keys of the action context which are:
   * <ul>
   * <li> <code>ActionContextConstants.SOURCE_VIEW_CONNECTOR</code> to get the
   * model value connector of the connector hierarchy.
   * </ul>
   * <p>
   * The returned connector mainly serves for retrieving the domain object the
   * action has to be triggered on.
   * 
   * @param context
   *            the action context.
   * @return the value connector this model action was triggered on.
   */
  public IValueConnector getSourceModelConnector(Map<String, Object> context) {
    if (context.get(ActionContextConstants.SOURCE_VIEW_CONNECTOR) != null) {
      return ((IValueConnector) context
          .get(ActionContextConstants.SOURCE_VIEW_CONNECTOR))
          .getModelConnector();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isBackend() {
    return true;
  }

  /**
   * Sets the nextAction.
   * 
   * @param nextAction
   *            the nextAction to set.
   */
  public void setNextAction(AbstractBackendAction nextAction) {
    this.nextAction = nextAction;
  }

  /**
   * Gets the beanAccessorFactory.
   * 
   * @param context
   *            the action context.
   * @return the beanAccessorFactory.
   */
  protected IAccessorFactory getBeanAccessorFactory(Map<String, Object> context) {
    return getController(context).getBeanAccessorFactory();
  }

  /**
   * Gets the mapAccessorFactory.
   * 
   * @param context
   *            the action context.
   * @return the mapAccessorFactory.
   */
  protected IAccessorFactory getMapAccessorFactory(Map<String, Object> context) {
    return getController(context).getMapAccessorFactory();
  }

  /**
   * Gets the correct accessor factory to access a target object properties.
   * 
   * @param target
   *            the target to access.
   * @param context
   *            the action context.
   * @return either the map or bean accessor factory.
   */
  protected IAccessorFactory getAccessorFactory(Object target,
      Map<String, Object> context) {
    if (target instanceof Map) {
      return getMapAccessorFactory(context);
    }
    return getBeanAccessorFactory(context);
  }

  /**
   * Gets the current application session.
   * 
   * @param context
   *            the action context.
   * @return the current application session.
   */
  protected IApplicationSession getApplicationSession(
      Map<String, Object> context) {
    return getController(context).getApplicationSession();
  }

  /**
   * Gets the beanConnectorFactory.
   * 
   * @param context
   *            the action context.
   * @return the beanConnectorFactory.
   */
  protected IModelConnectorFactory getBeanConnectorFactory(
      Map<String, Object> context) {
    return getController(context).getBeanConnectorFactory();
  }

  /**
   * Gets the frontend controller out of the action context.
   * 
   * @param context
   *            the action context.
   * @return the frontend controller.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected IBackendController getController(Map<String, Object> context) {
    return (IBackendController) context
        .get(ActionContextConstants.BACK_CONTROLLER);
  }

  /**
   * Gets the entityFactory.
   * 
   * @param context
   *            the action context.
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory(Map<String, Object> context) {
    return getController(context).getEntityFactory();
  }

  /**
   * Gets the nextAction.
   * 
   * @return the nextAction.
   */
  protected AbstractBackendAction getNextAction() {
    return nextAction;
  }
}
