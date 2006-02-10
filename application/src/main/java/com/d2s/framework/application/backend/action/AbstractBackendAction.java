/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

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

  private IAccessorFactory      accessorFactory;
  private IEntityFactory        entityFactory;
  private IBeanConnectorFactory beanConnectorFactory;

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
   * @return the value connector this model action was triggered on.
   */
  public IValueConnector getModelConnector() {
    return (IValueConnector) getContext().get(
        ActionContextConstants.MODEL_CONNECTOR);
  }

  /**
   * This is a utility method which is able to retrieve the model descriptor
   * this action has been executed on from its context. It uses well-known
   * context keys of the action context which is :
   * <ul>
   * <li> <code>ActionContextConstants.MODEL_DESCRIPTOR</code>.
   * </ul>
   * 
   * @return the model descriptor this action executes on.
   */
  public IModelDescriptor getModelDescriptor() {
    return (IModelDescriptor) getContext().get(
        ActionContextConstants.MODEL_DESCRIPTOR);
  }

  /**
   * This is a utility method which is able to retrieve the module model
   * connector this action has been executed on from its context. It uses
   * well-known context keys of the action context which is :
   * <ul>
   * <li> <code>ActionContextConstants.MODULE_MODEL_CONNECTOR</code>.
   * </ul>
   * 
   * @return the module model connector this action executes on.
   */
  public ICompositeValueConnector getModuleConnector() {
    return (ICompositeValueConnector) getContext().get(
        ActionContextConstants.MODULE_MODEL_CONNECTOR);
  }

  /**
   * Gets the accessorFactory.
   * 
   * @return the accessorFactory.
   */
  protected IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  /**
   * Sets the accessorFactory.
   * 
   * @param accessorFactory
   *          the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Gets the entityFactory.
   * 
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Sets the entityFactory.
   * 
   * @param entityFactory
   *          the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * Gets the beanConnectorFactory.
   * 
   * @return the beanConnectorFactory.
   */
  protected IBeanConnectorFactory getBeanConnectorFactory() {
    return beanConnectorFactory;
  }

  /**
   * Sets the beanConnectorFactory.
   * 
   * @param beanConnectorFactory
   *          the beanConnectorFactory to set.
   */
  public void setBeanConnectorFactory(IBeanConnectorFactory beanConnectorFactory) {
    this.beanConnectorFactory = beanConnectorFactory;
  }

}
