/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.application.AbstractController;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.bean.BeanConnector;
import com.d2s.framework.binding.bean.IBeanConnectorFactory;
import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IAction;
import com.d2s.framework.view.projection.Projection;

/**
 * Base class for backend application controllers. It provides the implementor
 * with commonly used accessors as well as a reference to the root model
 * descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendController extends AbstractController
    implements IBackendController {

  private IBeanConnectorFactory                 beanConnectorFactory;
  private Map<String, ICompositeValueConnector> rootProjectionConnectors;
  private IApplicationSession                   applicationSession;

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(IAction action) {
    if (action == null) {
      return null;
    }
    Map<String, Object> actionContext = getInitialActionContext();
    if (action.getContext() != null) {
      actionContext.putAll(action.getContext());
    }
    action.setContext(actionContext);
    return action.execute(this);
  }

  /**
   * {@inheritDoc}
   */
  public void start() {
    // Empty as of now
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

  /**
   * Contains the current application session.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<String, Object>();
    initialActionContext.put(ActionContextConstants.APPLICATION_SESSION,
        getApplicationSession());
    return initialActionContext;
  }

  /**
   * {@inheritDoc}
   */
  public ICompositeValueConnector getRootProjectionConnector(
      String rootProjectionId) {
    return rootProjectionConnectors.get(rootProjectionId);
  }

  /**
   * Sets the root model controller projections. These projections are not kept
   * as-is. Their connectors are.
   * 
   * @param rootProjections
   *          A map containing the root model projections indexed by a
   *          well-known key used to bind them with their projection views.
   */
  public void setRootProjections(Map<String, Projection> rootProjections) {
    rootProjectionConnectors = new HashMap<String, ICompositeValueConnector>();
    for (Map.Entry<String, Projection> projectionEntry : rootProjections
        .entrySet()) {
      BeanConnector nextProjectionConnector = beanConnectorFactory
          .createBeanConnector(projectionEntry.getKey(), Projection.class);
      nextProjectionConnector.setConnectorValue(projectionEntry.getValue());
      rootProjectionConnectors.put(projectionEntry.getKey(),
          nextProjectionConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector createModelConnector(IModelDescriptor modelDescriptor) {
    if (modelDescriptor instanceof ICollectionDescriptor) {
      return beanConnectorFactory.createBeanCollectionConnector(modelDescriptor
          .getName(), ((ICollectionDescriptor) modelDescriptor)
          .getElementDescriptor().getComponentContract());
    } else if (modelDescriptor instanceof IComponentDescriptor) {
      return beanConnectorFactory.createBeanConnector(
          modelDescriptor.getName(), ((IComponentDescriptor) modelDescriptor)
              .getComponentContract());
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public IApplicationSession getApplicationSession() {
    return applicationSession;
  }

  /**
   * Sets the applicationSession.
   * 
   * @param applicationSession
   *          the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    this.applicationSession = applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  public IEntity merge(IEntity entity, MergeMode mergeMode) {
    return getApplicationSession().merge(entity, mergeMode);
  }
}
