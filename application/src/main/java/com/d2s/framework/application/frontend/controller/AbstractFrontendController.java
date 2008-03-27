/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.d2s.framework.application.frontend.IFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.Workspace;
import com.d2s.framework.application.view.descriptor.IModuleViewDescriptorFactory;
import com.d2s.framework.application.view.descriptor.basic.WorkspaceCardViewDescriptor;
import com.d2s.framework.binding.ConnectorSelectionEvent;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorSelectionListener;
import com.d2s.framework.binding.IConnectorSelector;
import com.d2s.framework.binding.IMvcBinder;
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
import com.d2s.framework.view.descriptor.ISplitViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicSplitViewDescriptor;

/**
 * This class serves as base class for frontend (view) controllers.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public abstract class AbstractFrontendController<E, F, G> extends
    AbstractController implements IFrontendController<E, F, G> {

  /**
   * <code>MAX_LOGIN_RETRIES</code>.
   */
  protected static final int                    MAX_LOGIN_RETRIES = 3;

  private ActionMap                             actionMap;
  private IBackendController                    backendController;

  private DefaultIconDescriptor                 controllerDescriptor;
  private ActionMap                             helpActionMap;
  private CallbackHandler                       loginCallbackHandler;
  private String                                loginContextName;
  private IModuleViewDescriptorFactory          moduleViewDescriptorFactory;

  private IMvcBinder                            mvcBinder;

  private Map<String, ICompositeValueConnector> selectedModuleConnectors;

  private String                                selectedWorkspaceName;
  private IAction                               startupAction;

  private IViewFactory<E, F, G>                 viewFactory;

  private Map<String, Workspace>                workspaces;
  private String                                workspacesMenuIconImageUrl;

  /**
   * Constructs a new <code>AbstractFrontendController</code> instance.
   */
  public AbstractFrontendController() {
    controllerDescriptor = new DefaultIconDescriptor();
    selectedModuleConnectors = new HashMap<String, ICompositeValueConnector>();
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
        return executeBackend(action, context);
      }
      return executeFrontend(action, context);
    } catch (Throwable ex) {
      handleException(ex, context);
      return false;
    }
  }

  /**
   * Gets the actions.
   * 
   * @return the actions.
   */
  public ActionMap getActionMap() {
    return actionMap;
  }

  /**
   * {@inheritDoc}
   */
  public IApplicationSession getApplicationSession() {
    return getBackendController().getApplicationSession();
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
   * {@inheritDoc}
   */
  public String getDescription() {
    return controllerDescriptor.getDescription();
  }

  /**
   * Gets the help actions.
   * 
   * @return the help actions.
   */
  public ActionMap getHelpActions() {
    return helpActionMap;
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
   * {@inheritDoc}
   */
  public String getIconImageURL() {
    return controllerDescriptor.getIconImageURL();
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
        .get(getSelectedWorkspaceName());
    if (selectedModuleViewConnector != null) {
      initialActionContext.put(ActionContextConstants.MODULE_VIEW_CONNECTOR,
          selectedModuleViewConnector);
    }
    return initialActionContext;
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
   * Gets the mvcBinder.
   * 
   * @return the mvcBinder.
   */
  public IMvcBinder getMvcBinder() {
    return mvcBinder;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return controllerDescriptor.getName();
  }

  /**
   * {@inheritDoc}
   */
  public IAction getStartupAction() {
    return startupAction;
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
   * Sets the actionMap.
   * 
   * @param actionMap
   *            the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Sets the description.
   * 
   * @param description
   *            the description to set.
   */
  public void setDescription(String description) {
    controllerDescriptor.setDescription(description);
  }

  /**
   * Sets the helpActionMap.
   * 
   * @param helpActionMap
   *            the helpActionMap to set.
   */
  public void setHelpActionMap(ActionMap helpActionMap) {
    this.helpActionMap = helpActionMap;
  }

  /**
   * Sets the iconImageURL.
   * 
   * @param iconImageURL
   *            the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    controllerDescriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the loginContextName.
   * 
   * @param loginContextName
   *            the loginContextName to set.
   */
  public void setLoginContextName(String loginContextName) {
    this.loginContextName = loginContextName;
  }

  /**
   * Sets the moduleViewDescriptorFactory.
   * 
   * @param moduleViewDescriptorFactory
   *            the moduleViewDescriptorFactory to set.
   */
  public void setModuleViewDescriptorFactory(
      IModuleViewDescriptorFactory moduleViewDescriptorFactory) {
    this.moduleViewDescriptorFactory = moduleViewDescriptorFactory;
  }

  /**
   * Sets the mvcBinder.
   * 
   * @param mvcBinder
   *            the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Sets the name.
   * 
   * @param name
   *            the name to set.
   */
  public void setName(String name) {
    controllerDescriptor.setName(name);
  }

  /**
   * Sets the startupAction.
   * 
   * @param startupAction
   *            the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
  }

  /**
   * Sets the viewFactory.
   * 
   * @param viewFactory
   *            the viewFactory to set.
   */
  public void setViewFactory(IViewFactory<E, F, G> viewFactory) {
    this.viewFactory = viewFactory;
  }

  /**
   * Sets the workspaces. Modules are used by the frontend controller to give a
   * user access on the domain window.
   * 
   * @param workspaces
   *            the workspaces to set.
   */
  public void setWorkspaces(List<Workspace> workspaces) {
    this.workspaces = new LinkedHashMap<String, Workspace>();
    for (Workspace workspace : workspaces) {
      this.workspaces.put(workspace.getName(), workspace);
    }
  }

  /**
   * Sets the workspacesMenuIconImageUrl.
   * 
   * @param workspacesMenuIconImageUrl
   *            the workspacesMenuIconImageUrl to set.
   */
  public void setWorkspacesMenuIconImageUrl(String workspacesMenuIconImageUrl) {
    this.workspacesMenuIconImageUrl = workspacesMenuIconImageUrl;
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
   * {@inheritDoc}
   */
  public boolean stop() {
    return getBackendController().stop();
  }

  /**
   * Creates a new login callback handler.
   * 
   * @return a new login callback handler
   */
  protected abstract CallbackHandler createLoginCallbackHandler();

  /**
   * Creates a root workspace view.
   * 
   * @param workspaceName
   *            the identifier of the workspace to create the view for.
   * @param workspaceViewDescriptor
   *            the view descriptor of the workspace to render.
   * @param workspace
   *            the workspace to create the view for.
   * @return a view rendering the workspace.
   */
  protected IView<E> createWorkspaceView(final String workspaceName,
      IViewDescriptor workspaceViewDescriptor, Workspace workspace) {
    BasicSplitViewDescriptor splitViewDescriptor = new BasicSplitViewDescriptor();
    splitViewDescriptor.setOrientation(ISplitViewDescriptor.HORIZONTAL);
    splitViewDescriptor.setName(workspaceViewDescriptor.getName());
    // splitViewDescriptor.setDescription(workspaceViewDescriptor.getDescription());
    splitViewDescriptor.setIconImageURL(workspaceViewDescriptor
        .getIconImageURL());
    splitViewDescriptor.setCascadingModels(true);

    WorkspaceCardViewDescriptor workspacePaneDescriptor = new WorkspaceCardViewDescriptor(
        workspace, moduleViewDescriptorFactory);

    splitViewDescriptor.setLeftTopViewDescriptor(workspaceViewDescriptor);
    splitViewDescriptor.setRightBottomViewDescriptor(workspacePaneDescriptor);

    ICompositeView<E> workspaceView = (ICompositeView<E>) viewFactory
        .createView(splitViewDescriptor, this, getLocale());
    ((IConnectorSelector) workspaceView.getConnector())
        .addConnectorSelectionListener(new IConnectorSelectionListener() {

          public void selectedConnectorChange(ConnectorSelectionEvent event) {
            selectedModuleChanged(workspaceName,
                (ICompositeValueConnector) event.getSelectedConnector());
          }

        });
    for (IView<E> childView : workspaceView.getChildren()) {
      if (childView instanceof IMapView) {
        for (Map.Entry<String, IView<E>> grandChildView : ((IMapView<E>) childView)
            .getChildrenMap().entrySet()) {
          mvcBinder.bind(grandChildView.getValue().getConnector(),
              getBackendController().createModelConnector(
                  workspaceName + "_" + grandChildView.getKey(),
                  grandChildView.getValue().getDescriptor()
                      .getModelDescriptor()));
        }
      }
    }
    return workspaceView;
  }

  /**
   * Displays a workspace.
   * 
   * @param workspaceName
   *            the workspace identifier.
   */
  protected abstract void displayWorkspace(String workspaceName);

  /**
   * Executes a backend action.
   * 
   * @param action
   *            the backend action to execute.
   * @param context
   *            the action execution context.
   * @return true if the action was succesfully executed.
   */
  protected boolean executeBackend(IAction action, Map<String, Object> context) {
    return getBackendController().execute(action, context);
  }

  /**
   * Executes a frontend action.
   * 
   * @param action
   *            the frontend action to execute.
   * @param context
   *            the action execution context.
   * @return true if the action was succesfully executed.
   */
  protected boolean executeFrontend(IAction action, Map<String, Object> context) {
    return action.execute(this, context);
  }

  /**
   * Whenever the loginContextName is not configured, creates a default subject.
   * 
   * @return the default Subject in case the login configuration is not set.
   */
  protected Subject getAnonymousSubject() {
    return SecurityHelper.createAnonymousSubject();
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
   * Gets the loginContextName.
   * 
   * @return the loginContextName.
   */
  protected String getLoginContextName() {
    return loginContextName;
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
   * Gets the selectedWorkspaceName.
   * 
   * @return the selectedWorkspaceName.
   */
  protected String getSelectedWorkspaceName() {
    return selectedWorkspaceName;
  }

  /**
   * Given a workspace name, this method returns the associated workspace.
   * 
   * @param workspaceName
   *            the name of the workspace.
   * @return the selected workspace.
   */
  protected Workspace getWorkspace(String workspaceName) {
    if (workspaces != null) {
      return workspaces.get(workspaceName);
    }
    return null;
  }

  /**
   * Returns the list of workspace names. This list defines the set of
   * workspaces the user have access to.
   * 
   * @return the list of workspace names.
   */
  protected List<String> getWorkspaceNames() {
    if (workspaces != null) {
      return new ArrayList<String>(workspaces.keySet());
    }
    return Collections.<String> emptyList();
  }

  /**
   * Gets the workspacesMenuIconImageUrl.
   * 
   * @return the workspacesMenuIconImageUrl.
   */
  protected String getWorkspacesMenuIconImageUrl() {
    return workspacesMenuIconImageUrl;
  }

  /**
   * This method installs the security subject into the application session.
   * 
   * @param subject
   *            the authenticated user subject.
   */
  protected void loginSuccess(Subject subject) {
    getBackendController().getApplicationSession().setSubject(subject);
    String userPreferredLanguageCode = (String) getBackendController()
        .getApplicationSession().getPrincipal().getCustomProperty(
            UserPrincipal.LANGUAGE_PROPERTY);
    if (userPreferredLanguageCode != null) {
      getBackendController().getApplicationSession().setLocale(
          new Locale(userPreferredLanguageCode));
    }
    if (workspaces != null) {
      for (Workspace workspace : workspaces.values()) {
        translateWorkspace(workspace);
      }
      getBackendController().installWorkspaces(workspaces);
    }
  }

  /**
   * Sets the backend controller this controller is attached to.
   * 
   * @param backendController
   *            the backend controller to set.
   */
  protected void setBackendController(IBackendController backendController) {
    this.backendController = backendController;
  }

  /**
   * Sets the selectedWorkspaceName.
   * 
   * @param selectedWorkspaceName
   *            the selectedWorkspaceName to set.
   */
  protected void setSelectedWorkspaceName(String selectedWorkspaceName) {
    this.selectedWorkspaceName = selectedWorkspaceName;
  }

  private void selectedModuleChanged(String workspaceName,
      ICompositeValueConnector selectedConnector) {
    selectedModuleConnectors.put(workspaceName, selectedConnector);
    if (selectedConnector != null
        && selectedConnector.getConnectorValue() instanceof Module) {
      Module selectedModule = (Module) selectedConnector.getConnectorValue();
      if (!selectedModule.isStarted()
          && selectedModule.getStartupAction() != null) {
        execute(selectedModule.getStartupAction(), createEmptyContext());
        selectedModule.setStarted(true);
      }
    }
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

  private void translateWorkspace(Workspace workspace) {
    workspace.setI18nName(getTranslationProvider().getTranslation(
        workspace.getName(), getLocale()));
    workspace.setI18nDescription(getTranslationProvider().getTranslation(
        workspace.getDescription(), getLocale()));
    if (workspace.getModules() != null) {
      for (Module module : workspace.getModules()) {
        translateModule(module);
      }
    }
  }
}
