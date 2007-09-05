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
import com.d2s.framework.application.model.descriptor.ModuleDescriptor;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.model.datatransfer.ComponentTransferStructure;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.security.ISecurable;
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

  private IApplicationSession          applicationSession;
  private IAccessorFactory             beanAccessorFactory;
  private IModelConnectorFactory       beanConnectorFactory;
  private IEntityFactory               entityFactory;
  private IAccessorFactory             mapAccessorFactory;
  private IModelConnectorFactory       mapConnectorFactory;
  private Map<String, IValueConnector> moduleConnectors;

  private ComponentTransferStructure   transferStructure;

  /**
   * {@inheritDoc}
   */
  public void checkModuleAccess(String moduleId) {
    checkAccess((ISecurable) getModuleConnector(moduleId).getConnectorValue());
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector createModelConnector(IModelDescriptor modelDescriptor) {
    return beanConnectorFactory.createModelConnector(modelDescriptor);
  }

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
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
  public IApplicationSession getApplicationSession() {
    return applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  public IAccessorFactory getBeanAccessorFactory() {
    return beanAccessorFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IModelConnectorFactory getBeanConnectorFactory() {
    return beanConnectorFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IEntityFactory getEntityFactory() {
    return entityFactory;
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
  public IAccessorFactory getMapAccessorFactory() {
    return mapAccessorFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IModelConnectorFactory getMapConnectorFactory() {
    return mapConnectorFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getModuleConnector(String moduleId) {
    return moduleConnectors.get(moduleId);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unused")
  public void handleException(Throwable ex, Map<String, Object> context) {
    // NO-OP
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
  public ComponentTransferStructure retrieveComponents() {
    return transferStructure;
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
   * Sets the beanAccessorFactory.
   * 
   * @param beanAccessorFactory
   *          the beanAccessorFactory to set.
   */
  public void setBeanAccessorFactory(IAccessorFactory beanAccessorFactory) {
    this.beanAccessorFactory = beanAccessorFactory;
  }

  /**
   * Sets the beanConnectorFactory.
   * 
   * @param beanConnectorFactory
   *          the beanConnectorFactory to set.
   */
  public void setBeanConnectorFactory(
      IModelConnectorFactory beanConnectorFactory) {
    this.beanConnectorFactory = beanConnectorFactory;
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
   * Sets the mapAccessorFactory.
   * 
   * @param mapAccessorFactory
   *          the mapAccessorFactory to set.
   */
  public void setMapAccessorFactory(IAccessorFactory mapAccessorFactory) {
    this.mapAccessorFactory = mapAccessorFactory;
  }

  /**
   * Sets the mapConnectorFactory.
   * 
   * @param mapConnectorFactory
   *          the mapConnectorFactory to set.
   */
  public void setMapConnectorFactory(IModelConnectorFactory mapConnectorFactory) {
    this.mapConnectorFactory = mapConnectorFactory;
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
    moduleConnectors = new HashMap<String, IValueConnector>();
    for (Map.Entry<String, Module> moduleEntry : modules.entrySet()) {
      IValueConnector nextModuleConnector = beanConnectorFactory
          .createModelConnector(ModuleDescriptor.MODULE_DESCRIPTOR);
      nextModuleConnector.setConnectorValue(moduleEntry.getValue());
      moduleConnectors.put(moduleEntry.getKey(), nextModuleConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean start(Locale locale) {
    applicationSession.setLocale(locale);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void storeComponents(ComponentTransferStructure components) {
    this.transferStructure = components;
  }

  private void translateModule(Module module) {
    module.setI18nName(getTranslationProvider().getTranslation(
        module.getName(), getLocale()));
    module.setI18nDescription(getTranslationProvider().getTranslation(
        module.getDescription(), getLocale()));
    if (module.getSubModules() != null) {
      for (Module subModule : module.getSubModules()) {
        translateModule(subModule);
      }
    }
  }

  /**
   * Translate modules based on the locale set.
   */
  public void translateModules() {
    for (IValueConnector moduleConnector : moduleConnectors.values()) {
      translateModule((Module) moduleConnector.getConnectorValue());
    }
  }
}
