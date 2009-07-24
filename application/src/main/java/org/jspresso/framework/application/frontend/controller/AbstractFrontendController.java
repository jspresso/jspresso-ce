/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.frontend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.AbstractController;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.workspace.ExitAction;
import org.jspresso.framework.application.frontend.action.workspace.WorkspaceSelectionAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.view.descriptor.basic.WorkspaceCardViewDescriptor;
import org.jspresso.framework.binding.ConnectorSelectionEvent;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConnectorSelectionListener;
import org.jspresso.framework.binding.IConnectorSelector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.security.UsernamePasswordHandler;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.EOrientation;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicSplitViewDescriptor;

/**
 * This class serves as base class for frontend (view) controllers.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
    AbstractController implements IFrontendController<E, F, G> {

  /**
   * <code>MAX_LOGIN_RETRIES</code>.
   */
  protected static final int                    MAX_LOGIN_RETRIES = 3;

  private ActionMap                             actionMap;
  private IBackendController                    backendController;

  private Locale                                clientLocale;
  private DefaultIconDescriptor                 controllerDescriptor;
  private List<Map<String, Object>>             dialogContextStack;
  private String                                forcedStartingLocale;
  private ActionMap                             helpActionMap;

  private CallbackHandler                       loginCallbackHandler;

  private String                                loginContextName;

  private IMvcBinder                            mvcBinder;

  private Map<String, ICompositeValueConnector> selectedModuleConnectors;

  private String                                selectedWorkspaceName;
  private IAction                               startupAction;

  private IViewFactory<E, F, G>                 viewFactory;
  private Map<String, Workspace>                workspaces;

  private String                                workspacesMenuIconImageUrl;

  private IViewDescriptor                       loginViewDescriptor;

  /**
   * Constructs a new <code>AbstractFrontendController</code> instance.
   */
  public AbstractFrontendController() {
    controllerDescriptor = new DefaultIconDescriptor();
    selectedModuleConnectors = new HashMap<String, ICompositeValueConnector>();
    dialogContextStack = new ArrayList<Map<String, Object>>();
  }

  /**
   * {@inheritDoc}
   */
  public void displayModalDialog(@SuppressWarnings("unused") E mainView,
      @SuppressWarnings("unused") java.util.List<G> actions,
      @SuppressWarnings("unused") String title,
      @SuppressWarnings("unused") E sourceComponent,
      Map<String, Object> context,
      @SuppressWarnings("unused") Dimension dimension, boolean reuseCurrent) {
    if (!reuseCurrent || dialogContextStack.size() == 0) {
      dialogContextStack.add(0, context);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void displayWorkspace(String workspaceName) {
    getBackendController().checkWorkspaceAccess(workspaceName);
    Workspace workspace = getWorkspace(workspaceName);
    if (!workspace.isStarted()) {
      if (workspace.getStartupAction() != null) {
        Map<String, Object> actionContext = getInitialActionContext();
        actionContext.put(ActionContextConstants.ACTION_PARAM, workspace);
        execute(workspace.getStartupAction(), actionContext);
        workspace.setStarted(true);
      }
    }
    this.selectedWorkspaceName = workspaceName;
  }

  /**
   * {@inheritDoc}
   */
  public void disposeModalDialog(@SuppressWarnings("unused") E sourceWidget,
      Map<String, Object> context) {
    Map<String, Object> savedContext = dialogContextStack.remove(0);
    if (context != null && savedContext != null) {
      // preserve action param
      Object actionParam = context.get(ActionContextConstants.ACTION_PARAM);
      context.putAll(savedContext);
      context.put(ActionContextConstants.ACTION_PARAM, actionParam);
    }
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
   * {@inheritDoc}
   */
  public Locale getLocale() {
    if (getBackendController() != null) {
      return getBackendController().getApplicationSession().getLocale();
    }
    if (getForcedStartingLocale() != null) {
      return new Locale(getForcedStartingLocale());
    }
    return clientLocale;
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
   *          the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
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
   * Sets the forcedStartingLocale.
   * 
   * @param forcedStartingLocale
   *          the forcedStartingLocale to set.
   */
  public void setForcedStartingLocale(String forcedStartingLocale) {
    this.forcedStartingLocale = forcedStartingLocale;
  }

  /**
   * Sets the helpActionMap.
   * 
   * @param helpActionMap
   *          the helpActionMap to set.
   */
  public void setHelpActionMap(ActionMap helpActionMap) {
    this.helpActionMap = helpActionMap;
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
   * Sets the loginContextName.
   * 
   * @param loginContextName
   *          the loginContextName to set.
   */
  public void setLoginContextName(String loginContextName) {
    this.loginContextName = loginContextName;
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
   * Sets the name.
   * 
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    controllerDescriptor.setName(name);
  }

  /**
   * Sets the startupAction.
   * 
   * @param startupAction
   *          the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
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
   * Sets the workspaces. Modules are used by the frontend controller to give a
   * user access on the domain window.
   * 
   * @param workspaces
   *          the workspaces to set.
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
   *          the workspacesMenuIconImageUrl to set.
   */
  public void setWorkspacesMenuIconImageUrl(String workspacesMenuIconImageUrl) {
    this.workspacesMenuIconImageUrl = workspacesMenuIconImageUrl;
  }

  /**
   * Sets the loginViewDescriptor.
   * 
   * @param loginViewDescriptor
   *          the loginViewDescriptor to set.
   */
  public void setLoginViewDescriptor(IViewDescriptor loginViewDescriptor) {
    this.loginViewDescriptor = loginViewDescriptor;
  }

  /**
   * Binds to the backend controller and ask it to start.
   * <p>
   * {@inheritDoc}
   */
  public boolean start(IBackendController peerController, Locale theClientLocale) {
    this.clientLocale = theClientLocale;
    setBackendController(peerController);
    Locale initialLocale = theClientLocale;
    if (forcedStartingLocale != null) {
      initialLocale = new Locale(forcedStartingLocale);
    }
    return peerController.start(initialLocale);
  }

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    selectedModuleConnectors = new HashMap<String, ICompositeValueConnector>();
    selectedWorkspaceName = null;
    return getBackendController().stop();
  }

  /**
   * Creates a new login callback handler.
   * 
   * @return a new login callback handler
   */
  protected CallbackHandler createLoginCallbackHandler() {
    return new UsernamePasswordHandler();
  }

  /**
   * Creates and binds the login view.
   * 
   * @return the login view
   */
  protected IView<E> createLoginView() {
    IView<E> loginView = getViewFactory().createView(getLoginViewDescriptor(),
        this, getLocale());
    IValueConnector loginModelConnector = getBackendController()
        .createModelConnector("login",
            getLoginViewDescriptor().getModelDescriptor());
    getMvcBinder().bind(loginView.getConnector(), loginModelConnector);
    loginModelConnector.setConnectorValue(getLoginCallbackHandler());
    return loginView;
  }

  /**
   * Creates the workspace action map.
   * 
   * @return the workspace action map.
   */
  protected ActionMap createWorkspaceActionMap() {
    ActionMap workspaceActionMap = new ActionMap();
    List<ActionList> workspaceActionLists = new ArrayList<ActionList>();
    ActionList workspaceSelectionActionList = new ActionList();
    workspaceSelectionActionList.setName("workspaces");
    workspaceSelectionActionList
        .setIconImageURL(getWorkspacesMenuIconImageUrl());
    List<IDisplayableAction> workspaceSelectionActions = new ArrayList<IDisplayableAction>();
    for (String workspaceName : getWorkspaceNames()) {
      WorkspaceSelectionAction<E, F, G> workspaceSelectionAction = new WorkspaceSelectionAction<E, F, G>();
      IViewDescriptor workspaceViewDescriptor = getWorkspace(workspaceName)
          .getViewDescriptor();
      workspaceSelectionAction.setWorkspaceName(workspaceName);
      workspaceSelectionAction.setName(workspaceViewDescriptor.getName());
      workspaceSelectionAction.setDescription(workspaceViewDescriptor
          .getDescription());
      workspaceSelectionAction.setIconImageURL(workspaceViewDescriptor
          .getIconImageURL());
      workspaceSelectionActions.add(workspaceSelectionAction);
    }
    workspaceSelectionActionList.setActions(workspaceSelectionActions);

    ActionList exitActionList = new ActionList();
    exitActionList.setName("QUIT");
    List<IDisplayableAction> exitActions = new ArrayList<IDisplayableAction>();
    ExitAction<E, F, G> exitAction = new ExitAction<E, F, G>();
    exitAction.setName("quit.name");
    exitAction.setDescription("quit.description");
    exitAction.setIconImageURL(getViewFactory().getIconFactory()
        .getCancelIconImageURL());
    exitActions.add(exitAction);
    exitActionList.setActions(exitActions);

    workspaceActionLists.add(workspaceSelectionActionList);
    workspaceActionLists.add(exitActionList);
    workspaceActionMap.setActionLists(workspaceActionLists);

    return workspaceActionMap;
  }

  /**
   * Creates a root workspace view.
   * 
   * @param workspaceName
   *          the identifier of the workspace to create the view for.
   * @param workspaceViewDescriptor
   *          the view descriptor of the workspace to render.
   * @param workspace
   *          the workspace to create the view for.
   * @return a view rendering the workspace.
   */
  protected IView<E> createWorkspaceView(final String workspaceName,
      IViewDescriptor workspaceViewDescriptor, Workspace workspace) {
    BasicSplitViewDescriptor splitViewDescriptor = new BasicSplitViewDescriptor();
    splitViewDescriptor.setOrientation(EOrientation.HORIZONTAL);
    splitViewDescriptor.setName(workspaceViewDescriptor.getName());
    // splitViewDescriptor.setDescription(workspaceViewDescriptor.getDescription(
    // ));
    splitViewDescriptor.setIconImageURL(workspaceViewDescriptor
        .getIconImageURL());
    splitViewDescriptor.setCascadingModels(true);

    splitViewDescriptor.setLeftTopViewDescriptor(workspaceViewDescriptor);
    splitViewDescriptor
        .setRightBottomViewDescriptor(new WorkspaceCardViewDescriptor());

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
      if (childView instanceof IMapView<?>) {
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
   * Executes a backend action.
   * 
   * @param action
   *          the backend action to execute.
   * @param context
   *          the action execution context.
   * @return true if the action was succesfully executed.
   */
  protected boolean executeBackend(IAction action, Map<String, Object> context) {
    return getBackendController().execute(action, context);
  }

  /**
   * Executes a frontend action.
   * 
   * @param action
   *          the frontend action to execute.
   * @param context
   *          the action execution context.
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
   * Gets the forcedStartingLocale.
   * 
   * @return the forcedStartingLocale.
   */
  protected String getForcedStartingLocale() {
    return forcedStartingLocale;
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
   *          the name of the workspace.
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
   *          the authenticated user subject.
   */
  protected void loginSuccess(Subject subject) {
    if (getLoginCallbackHandler() instanceof UsernamePasswordHandler) {
      ((UsernamePasswordHandler) getLoginCallbackHandler()).clear();
    }
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
   * Performs the actual JAAS login.
   * 
   * @return true if login succeeded.
   */
  protected boolean performLogin() {
    if (getLoginContextName() != null) {
      try {
        LoginContext lc = null;
        try {
          lc = new LoginContext(getLoginContextName(),
              getLoginCallbackHandler());
        } catch (LoginException le) {
          System.err.println("Cannot create LoginContext. " + le.getMessage());
          return false;
        } catch (SecurityException se) {
          System.err.println("Cannot create LoginContext. " + se.getMessage());
          return false;
        }
        lc.login();
        loginSuccess(lc.getSubject());
      } catch (LoginException le) {
        System.err.println("Authentication failed:");
        System.err.println("  " + le.getMessage());
        return false;
      }
    } else {
      loginSuccess(getAnonymousSubject());
    }
    return true;
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

  /**
   * Gets the loginViewDescriptor.
   * 
   * @return the loginViewDescriptor.
   */
  protected IViewDescriptor getLoginViewDescriptor() {
    return loginViewDescriptor;
  }
}
