/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.application.AbstractController;
import com.d2s.framework.application.backend.entity.ApplicationSessionAwareProxyEntityFactory;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.backend.session.basic.BasicApplicationSession;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.bean.BeanConnector;
import com.d2s.framework.binding.bean.IBeanConnectorFactory;
import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.security.SecurityHelper;
import com.d2s.framework.util.accessor.IAccessorFactory;

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

  private IAccessorFactory                      accessorFactory;
  private IEntityFactory                        entityFactory;
  private IBeanConnectorFactory                 beanConnectorFactory;
  private Map<String, ICompositeValueConnector> moduleConnectors;
  private IApplicationSession                   applicationSession;

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return false;
    }
    SecurityHelper.checkAccess(getApplicationSession().getSubject(), action,
        getTranslationProvider(), getLocale());
    Map<String, Object> actionContext = getInitialActionContext();
    if (context != null) {
      context.putAll(actionContext);
    }
    return action.execute(this, context);
  }

  /**
   * {@inheritDoc}
   */
  public boolean start(Locale locale) {
    applicationSession.setLocale(locale);
    for (ICompositeValueConnector moduleConnector : moduleConnectors.values()) {
      translateModule((Module) moduleConnector.getConnectorValue());
    }
    return true;
  }

  private void translateModule(Module module) {
    module.setName(getTranslationProvider().getTranslation(module.getName(),
        getLocale()));
    module.setDescription(getTranslationProvider().getTranslation(
        module.getDescription(), getLocale()));
    if (module.getSubModules() != null) {
      for (Module subModule : module.getSubModules()) {
        translateModule(subModule);
      }
    }
  }

  /**
   * Gets the locale used by this controller. The locale is actually held by the
   * session.
   * 
   * @return locale used by this controller.
   */
  public Locale getLocale() {
    return applicationSession.getLocale();
  }

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    return true;
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
    initialActionContext.put(ActionContextConstants.BACK_CONTROLLER, this);
    return initialActionContext;
  }

  /**
   * {@inheritDoc}
   */
  public ICompositeValueConnector getModuleConnector(String moduleId) {
    return moduleConnectors.get(moduleId);
  }

  /**
   * Sets the model controller modules. These modules are not kept as-is. Their
   * connectors are.
   * 
   * @param modules
   *          A map containing the modules indexed by a well-known key used to
   *          bind them with their views.
   */
  public void setModules(Map<String, Module> modules) {
    moduleConnectors = new HashMap<String, ICompositeValueConnector>();
    for (Map.Entry<String, Module> moduleEntry : modules.entrySet()) {
      BeanConnector nextModuleConnector = beanConnectorFactory
          .createBeanConnector(moduleEntry.getKey(), Module.class);
      nextModuleConnector.setConnectorValue(moduleEntry.getValue());
      moduleConnectors.put(moduleEntry.getKey(), nextModuleConnector);
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
    if (!(applicationSession instanceof BasicApplicationSession)) {
      throw new IllegalArgumentException(
          "applicationSession must be a BasicApplicationSession.");
    }
    this.applicationSession = applicationSession;
    linkSessionArtifacts();
  }

  /**
   * {@inheritDoc}
   */
  public IEntity merge(IEntity entity, MergeMode mergeMode) {
    return getApplicationSession().merge(entity, mergeMode);
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> merge(List<IEntity> entities, MergeMode mergeMode) {
    return getApplicationSession().merge(entities, mergeMode);
  }

  /**
   * {@inheritDoc}
   */
  public IBeanConnectorFactory getBeanConnectorFactory() {
    return beanConnectorFactory;
  }

  /**
   * Sets the entityFactory.
   * 
   * @param entityFactory
   *          the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    if (!(entityFactory instanceof ApplicationSessionAwareProxyEntityFactory)) {
      throw new IllegalArgumentException(
          "entityFactory must be an ApplicationSessionAwareProxyEntityFactory.");
    }
    this.entityFactory = entityFactory;
    linkSessionArtifacts();
  }

  /**
   * {@inheritDoc}
   */
  public IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IAccessorFactory getAccessorFactory() {
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

  private void linkSessionArtifacts() {
    if (getApplicationSession() != null && getEntityFactory() != null) {
      ((ApplicationSessionAwareProxyEntityFactory) getEntityFactory())
          .setApplicationSession(getApplicationSession());
      ((BasicApplicationSession) getApplicationSession())
          .setEntityFactory(getEntityFactory());
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void handleException(Throwable ex, Map<String, Object> context) {
    // NO-OP
  }
}
