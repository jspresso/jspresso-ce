/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.application.AbstractController;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.IFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.view.descriptor.IModuleDescriptor;
import com.d2s.framework.application.view.descriptor.basic.ModuleCardViewDescriptor;
import com.d2s.framework.binding.ConnectorSelectionEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorSelectionListener;
import com.d2s.framework.binding.IConnectorSelector;
import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.security.SecurityHelper;
import com.d2s.framework.security.UserPrincipal;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.ICompositeView;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IMapView;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.ActionMap;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.ISplitViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicSplitViewDescriptor;

/**
 * This class serves as base class for frontend (view) controllers.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class AbstractFrontendController<E, F, G> extends
    AbstractController implements IFrontendController {

  private DefaultIconDescriptor                 controllerDescriptor;

  private Map<String, ICompositeValueConnector> selectedModuleConnectors;

  private IBackendController                    backendController;
  private Map<String, IModuleDescriptor>        moduleDescriptors;
  private String                                modulesMenuIconImageUrl;
  private IViewFactory<E, F, G>                 viewFactory;
  private IMvcBinder                            mvcBinder;

  private String                                loginContextName;
  private CallbackHandler                       loginCallbackHandler;

  private ActionMap                             actionMap;

  private String                                selectedModuleId;

  /**
   * <code>MAX_LOGIN_RETRIES</code>.
   */
  protected static final int                    MAX_LOGIN_RETRIES = 3;

  /**
   * Constructs a new <code>AbstractFrontendController</code> instance.
   */
  public AbstractFrontendController() {
    controllerDescriptor = new DefaultIconDescriptor();
    selectedModuleConnectors = new HashMap<String, ICompositeValueConnector>();
  }

  /**
   * Gets the peer model controller.
   *
   * @return the backend controller this frontend controller is attached to.
   */
  public IBackendController getBackendController() {
    return backendController;
  }

  /**
   * Sets the backend controller this controller is attached to.
   *
   * @param backendController
   *          the backend controller to set.
   */
  protected void setBackendController(IBackendController backendController) {
    this.backendController = backendController;
  }

  /**
   * Executes frontend actions and delegates backend actions execution to its
   * peer backend controller.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    Map<String, Object> actionContext = getInitialActionContext();
    context.putAll(actionContext);
    try {
      SecurityHelper.checkAccess(getBackendController().getApplicationSession()
          .getSubject(), action, getTranslationProvider(), getLocale());
      if (action.isBackend()) {
        executeBackend(action, context);
      } else {
        executeFrontend(action, context);
      }
    } catch (Throwable ex) {
      handleException(ex, context);
      return false;
    }
    return true;
  }

  /**
   * Executes a backend action.
   *
   * @param action
   *          the backend action to execute.
   * @param context
   *          the action execution context.
   */
  protected void executeBackend(IAction action, Map<String, Object> context) {
    getBackendController().execute(action, context);
  }

  /**
   * Executes a frontend action.
   *
   * @param action
   *          the frontend action to execute.
   * @param context
   *          the action execution context.
   */
  protected void executeFrontend(IAction action, Map<String, Object> context) {
    action.execute(this, context);
  }

  /**
   * {@inheritDoc}
   */
  public String getDescription() {
    return controllerDescriptor.getDescription();
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL() {
    return controllerDescriptor.getIconImageURL();
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return controllerDescriptor.getName();
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    controllerDescriptor.setDescription(description);
  }

  /**
   * Sets the iconImageURL.
   *
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    controllerDescriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    controllerDescriptor.setName(name);
  }

  /**
   * Binds to the backend controller and ask it to start.
   * <p>
   * {@inheritDoc}
   */
  public boolean start(IBackendController peerController, Locale startingLocale) {
    setBackendController(peerController);
    return peerController.start(startingLocale);
  }

  /**
   * Contains nothing.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<String, Object>();
    initialActionContext.put(ActionContextConstants.FRONT_CONTROLLER, this);
    ICompositeValueConnector selectedModuleViewConnector = selectedModuleConnectors
        .get(getSelectedModuleId());
    if (selectedModuleViewConnector != null) {
      initialActionContext.put(ActionContextConstants.MODULE_VIEW_CONNECTOR,
          selectedModuleViewConnector);
      if (selectedModuleViewConnector instanceof ICollectionConnectorProvider) {
        ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) selectedModuleViewConnector)
            .getCollectionConnector();
        if (collectionConnector != null) {
          collectionConnector.setAllowLazyChildrenLoading(false);
        }
      }
    }
    return initialActionContext;
  }

  /**
   * Sets the moduleDescriptors. Module view descriptors are used by the
   * frontend controller to give a user access on the domain window.
   *
   * @param moduleDescriptors
   *          the moduleDescriptors to set.
   */
  public void setModuleDescriptors(
      Map<String, IModuleDescriptor> moduleDescriptors) {
    this.moduleDescriptors = moduleDescriptors;
  }

  /**
   * Sets the viewFactory.
   *
   * @param viewFactory
   *          the viewFactory to set.
   */
  public void setViewFactory(IViewFactory<E, F, G> viewFactory) {
    this.viewFactory = viewFactory;
  }

  /**
   * Gets the viewFactory.
   *
   * @return the viewFactory.
   */
  public IViewFactory<E, F, G> getViewFactory() {
    return viewFactory;
  }

  /**
   * Given a well-kown module identifier, this method returns the associated
   * module view descriptor.
   *
   * @param moduleId
   *          the identifier of the module.
   * @return the view descriptor of the selected module.
   */
  protected IModuleDescriptor getModuleDescriptor(String moduleId) {
    return moduleDescriptors.get(moduleId);
  }

  /**
   * Returns the list of module identifiers. This list defines the set of
   * modules the user have access to.
   *
   * @return the list of module identifiers.
   */
  protected List<String> getModuleIds() {
    return new ArrayList<String>(moduleDescriptors.keySet());
  }

  /**
   * Gets the mvcBinder.
   *
   * @return the mvcBinder.
   */
  public IMvcBinder getMvcBinder() {
    return mvcBinder;
  }

  /**
   * Sets the mvcBinder.
   *
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Gets the locale.
   *
   * @return the locale.
   */
  public Locale getLocale() {
    return getBackendController().getApplicationSession().getLocale();
  }

  /**
   * Gets the selectedModuleConnectors.
   *
   * @return the selectedModuleConnectors.
   */
  protected Map<String, ICompositeValueConnector> getSelectedModuleConnectors() {
    return selectedModuleConnectors;
  }

  /**
   * Gets the iconFactory.
   *
   * @return the iconFactory.
   */
  protected IIconFactory<F> getIconFactory() {
    return viewFactory.getIconFactory();
  }

  /**
   * {@inheritDoc}
   */
  public IEntity merge(IEntity entity, MergeMode mergeMode) {
    return getBackendController().merge(entity, mergeMode);
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> merge(List<IEntity> entities, MergeMode mergeMode) {
    return getBackendController().merge(entities, mergeMode);
  }

  /**
   * Creates a root module view.
   *
   * @param moduleId
   *          the identifier of the module to create the view for.
   * @param moduleDescriptor
   *          the view descriptor of the module to render.
   * @param module
   *          the module to create the view for.
   * @return a view rendering the module.
   */
  protected IView<E> createModuleView(final String moduleId,
      IModuleDescriptor moduleDescriptor, Module module) {
    BasicSplitViewDescriptor splitViewDescriptor = new BasicSplitViewDescriptor();
    splitViewDescriptor.setOrientation(ISplitViewDescriptor.HORIZONTAL);
    splitViewDescriptor.setName(moduleDescriptor.getName());
    splitViewDescriptor.setMasterDetail(true);

    ModuleCardViewDescriptor modulePaneDescriptor = new ModuleCardViewDescriptor(
        module);

    splitViewDescriptor.setLeftTopViewDescriptor(moduleDescriptor);
    splitViewDescriptor.setRightBottomViewDescriptor(modulePaneDescriptor);

    ICompositeView<E> moduleView = (ICompositeView<E>) viewFactory.createView(
        splitViewDescriptor, this, getLocale());
    ((IConnectorSelector) moduleView.getConnector())
        .addConnectorSelectionListener(new IConnectorSelectionListener() {

          public void selectedConnectorChange(ConnectorSelectionEvent event) {
            selectedModuleConnectors.put(moduleId,
                (ICompositeValueConnector) event.getSelectedConnector());
          }
        });
    for (IView<E> childView : moduleView.getChildren()) {
      if (childView instanceof IMapView) {
        for (IView<E> grandChildView : ((IMapView<E>) childView).getChildren()) {
          mvcBinder.bind(grandChildView.getConnector(), getBackendController()
              .createModelConnector(
                  grandChildView.getDescriptor().getModelDescriptor()));
        }
      }
    }
    return moduleView;
  }

  /**
   * Gets the modulesMenuIconImageUrl.
   *
   * @return the modulesMenuIconImageUrl.
   */
  protected String getModulesMenuIconImageUrl() {
    return modulesMenuIconImageUrl;
  }

  /**
   * Sets the modulesMenuIconImageUrl.
   *
   * @param modulesMenuIconImageUrl
   *          the modulesMenuIconImageUrl to set.
   */
  public void setModulesMenuIconImageUrl(String modulesMenuIconImageUrl) {
    this.modulesMenuIconImageUrl = modulesMenuIconImageUrl;
  }

  /**
   * Sets the loginContextName.
   *
   * @param loginContextName
   *          the loginContextName to set.
   */
  public void setLoginContextName(String loginContextName) {
    this.loginContextName = loginContextName;
  }

  /**
   * Gets the loginCallbackHandler.
   *
   * @return the loginCallbackHandler.
   */
  protected CallbackHandler getLoginCallbackHandler() {
    if (loginCallbackHandler == null) {
      loginCallbackHandler = createLoginCallbackHandler();
    }
    return loginCallbackHandler;
  }

  /**
   * Creates a new login callback handler.
   *
   * @return a new login callback handler
   */
  protected abstract CallbackHandler createLoginCallbackHandler();

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    return getBackendController().stop();
  }

  /**
   * Gets the loginContextName.
   *
   * @return the loginContextName.
   */
  protected String getLoginContextName() {
    return loginContextName;
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale) {
    return controllerDescriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getI18nName(ITranslationProvider translationProvider,
      Locale locale) {
    return controllerDescriptor.getI18nName(translationProvider, locale);
  }

  /**
   * This method installs the security subject into the application session.
   *
   * @param subject
   *          the authenticated user subject.
   */
  protected void loginSuccess(Subject subject) {
    getBackendController().getApplicationSession().setSubject(subject);
    String userPreferredLanguageCode = (String) getBackendController()
        .getApplicationSession().getPrincipal().getCustomProperty(
            UserPrincipal.LANGUAGE_PROPERTY);
    if (userPreferredLanguageCode != null) {
      getBackendController().getApplicationSession().setLocale(
          new Locale(userPreferredLanguageCode));
      getBackendController().translateModules();
    }
  }

  /**
   * Gets the actionMap.
   *
   * @return the actionMap.
   */
  public Map<String, List<IDisplayableAction>> getActions() {
    if (actionMap != null) {
      return actionMap.getActionMap();
    }
    return null;
  }

  /**
   * Sets the actionMap.
   *
   * @param actionMap
   *          the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * {@inheritDoc}
   */
  public IApplicationSession getApplicationSession() {
    return getBackendController().getApplicationSession();
  }

  /**
   * Sets the selectedModuleId.
   *
   * @param selectedModuleId
   *          the selectedModuleId to set.
   */
  protected void setSelectedModuleId(String selectedModuleId) {
    this.selectedModuleId = selectedModuleId;
  }

  /**
   * Gets the selectedModuleId.
   *
   * @return the selectedModuleId.
   */
  protected String getSelectedModuleId() {
    return selectedModuleId;
  }
}
