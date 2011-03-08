/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.hibernate.exception.ConstraintViolationException;
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
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorListProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.security.UsernamePasswordHandler;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.preferences.IPreferencesStore;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Base class for frontend application controllers. Frontend controllers are
 * responsible for adapting the Jspresso application to the UI channel. Although
 * this generic abstract class centralizes most of the controller's
 * configuration, it will be subclassed by concrete, UI dependent subclasses to
 * implement polymorphic behaviour.
 * <p>
 * More than a behavioural adapter, the frontend controller will also be the
 * place where you define the top-level application structure like the workspace
 * list, the name, the application-wide actions, ...
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
  private ActionMap                             secondaryActionMap;
  private IBackendController                    backendController;

  private List<ModuleHistoryEntry>              backwardHistoryEntries;
  private Locale                                clientLocale;
  private DefaultIconDescriptor                 controllerDescriptor;
  private List<Map<String, Object>>             dialogContextStack;
  private IDisplayableAction                    exitAction;

  private String                                forcedStartingLocale;

  private List<ModuleHistoryEntry>              forwardHistoryEntries;

  private ActionMap                             helpActionMap;
  private ActionMap                             navigationActionMap;

  private CallbackHandler                       loginCallbackHandler;

  private String                                loginContextName;
  private IViewDescriptor                       loginViewDescriptor;
  private Map<String, IMapView<E>>              workspaceViews;
  private boolean                               moduleAutoPinEnabled;

  private IMvcBinder                            mvcBinder;
  private IAction                               onModuleEnterAction;

  private IAction                               onModuleExitAction;

  private Map<String, Module>                   selectedModules;

  private String                                selectedWorkspaceName;
  private IAction                               startupAction;

  private boolean                               tracksWorkspaceNavigator;
  private IViewFactory<E, F, G>                 viewFactory;
  private Map<String, ICompositeValueConnector> workspaceNavigatorConnectors;
  private Map<String, Workspace>                workspaces;
  private String                                workspacesMenuIconImageUrl;

  private Integer                               frameWidth;
  private Integer                               frameHeight;

  private static final String                   UP_KEY            = "UP_KEY";
  private static final String                   UP_SEP            = "!";

  private IPreferencesStore                     clientPreferenceStore;

  /**
   * Constructs a new <code>AbstractFrontendController</code> instance.
   */
  public AbstractFrontendController() {
    controllerDescriptor = new DefaultIconDescriptor();
    selectedModules = new HashMap<String, Module>();
    dialogContextStack = new ArrayList<Map<String, Object>>();
    workspaceNavigatorConnectors = new HashMap<String, ICompositeValueConnector>();
    workspaceViews = new HashMap<String, IMapView<E>>();
    backwardHistoryEntries = new LinkedList<ModuleHistoryEntry>();
    forwardHistoryEntries = new LinkedList<ModuleHistoryEntry>();
    moduleAutoPinEnabled = true;
    tracksWorkspaceNavigator = true;
  }

  /**
   * {@inheritDoc}
   */
  public void displayModule(Module module) {
    displayModule(getSelectedWorkspaceName(), module);
  }

  /**
   * {@inheritDoc}
   */
  public void displayModule(String workspaceName, Module module) {
    Module currentModule = selectedModules.get(getSelectedWorkspaceName());
    if ((currentModule == null && module == null)
        || ObjectUtils.equals(currentModule, module)) {
      return;
    }
    if (currentModule != null) {
      pinModule(getSelectedWorkspaceName(), currentModule);
      execute(currentModule.getExitAction(), createEmptyContext());
      execute(getOnModuleExitAction(), createEmptyContext());
    }
    displayWorkspace(workspaceName);
    IView<E> moduleAreaView = workspaceViews.get(workspaceName);
    if (moduleAreaView != null) {

      // The following does not seem necessary anymore.
      // This was done to cope with connectors mis-refreshing.

      // IValueConnector oldModuleModelConnector = moduleAreaViewConnector
      // .getModelConnector();
      // if (oldModuleModelConnector != null) {
      // oldModuleModelConnector.setConnectorValue(null);
      // }

      IValueConnector moduleModelConnector = getBackendController()
          .getModuleConnector(module);
      mvcBinder.bind(moduleAreaView.getConnector(), moduleModelConnector);
    }
    selectedModules.put(workspaceName, module);
    if (module != null) {
      if (!module.isStarted() && module.getStartupAction() != null) {
        execute(module.getStartupAction(), createEmptyContext());
      }
      module.setStarted(true);
      pinModule(getSelectedWorkspaceName(), module);
      execute(module.getEntryAction(), createEmptyContext());
      execute(getOnModuleEnterAction(), createEmptyContext());
    }
    boolean wasTracksWorkspaceNavigator = tracksWorkspaceNavigator;
    try {
      tracksWorkspaceNavigator = false;
      ICompositeValueConnector workspaceNavigatorConnector = workspaceNavigatorConnectors
          .get(workspaceName);
      if (workspaceNavigatorConnector instanceof ICollectionConnectorListProvider) {
        Object[] result = synchWorkspaceNavigatorSelection(
            (ICollectionConnectorListProvider) workspaceNavigatorConnector,
            module);
        if (result != null) {
          int moduleModelIndex = ((Integer) result[1]).intValue();
          ((ICollectionConnector) result[0]).setSelectedIndices(new int[] {
            moduleModelIndex
          }, moduleModelIndex);
        }
      }
    } finally {
      tracksWorkspaceNavigator = wasTracksWorkspaceNavigator;
    }
  }

  /**
   * {@inheritDoc}
   */
  public void displayNextPinnedModule() {
    boolean wasAutoPinEnabled = moduleAutoPinEnabled;
    try {
      moduleAutoPinEnabled = false;
      if (forwardHistoryEntries.size() > 0) {
        ModuleHistoryEntry nextEntry = forwardHistoryEntries.remove(0);
        String nextWorkspaceName = nextEntry.getWorkspaceName();
        Module nextModule = nextEntry.getModule();
        if (nextWorkspaceName != null && nextModule != null) {
          backwardHistoryEntries.add(nextEntry);
          if (ObjectUtils.equals(nextWorkspaceName, getSelectedWorkspaceName())
              && ObjectUtils.equals(nextModule,
                  getSelectedModule(getSelectedWorkspaceName()))) {
            displayNextPinnedModule();
          } else {
            displayModule(nextWorkspaceName, nextModule);
          }
        } else {
          displayNextPinnedModule();
        }
      }
    } finally {
      moduleAutoPinEnabled = wasAutoPinEnabled;
    }
  }

  /**
   * {@inheritDoc}
   */
  public void displayPreviousPinnedModule() {
    boolean wasAutoPinEnabled = moduleAutoPinEnabled;
    try {
      moduleAutoPinEnabled = false;
      if (backwardHistoryEntries.size() > 0) {
        ModuleHistoryEntry previousEntry = backwardHistoryEntries
            .remove(backwardHistoryEntries.size() - 1);
        String previousWorkspaceName = previousEntry.getWorkspaceName();
        Module previousModule = previousEntry.getModule();
        if (previousWorkspaceName != null && previousModule != null) {
          forwardHistoryEntries.add(0, previousEntry);
          if (ObjectUtils.equals(previousWorkspaceName,
              getSelectedWorkspaceName())
              && ObjectUtils.equals(previousModule,
                  getSelectedModule(getSelectedWorkspaceName()))) {
            displayPreviousPinnedModule();
          } else {
            displayModule(previousWorkspaceName, previousModule);
          }
        } else {
          displayPreviousPinnedModule();
        }
      }
    } finally {
      moduleAutoPinEnabled = wasAutoPinEnabled;
    }
  }

  /**
   * {@inheritDoc}
   */
  public void displayWorkspace(String workspaceName) {
    if (workspaceName != null) {
      Workspace workspace = getWorkspace(workspaceName);
      checkAccess(workspace);
      if (!workspace.isStarted()) {
        if (workspace.getStartupAction() != null) {
          Map<String, Object> actionContext = getInitialActionContext();
          actionContext.put(ActionContextConstants.ACTION_PARAM, workspace);
          execute(workspace.getStartupAction(), actionContext);
          workspace.setStarted(true);
        }
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
    // Retain only entries from the initial action context that are not in the
    // action context.
    actionContext.putAll(context);
    context.putAll(actionContext);
    try {
      checkAccess(action);
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
   * {@inheritDoc}
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
   * Gets the navigation actions.
   * 
   * @return the navigation actions.
   */
  public ActionMap getNavigationActions() {
    return navigationActionMap;
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
    if (dialogContextStack != null) {
      for (int i = dialogContextStack.size() - 1; i >= 0; i--) {
        initialActionContext.putAll(dialogContextStack.get(i));
      }
    }
    initialActionContext.put(ActionContextConstants.FRONT_CONTROLLER, this);
    initialActionContext.put(ActionContextConstants.MODULE,
        selectedModules.get(getSelectedWorkspaceName()));
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
   * Gets the selectedWorkspaceName.
   * 
   * @return the selectedWorkspaceName.
   */
  public String getSelectedWorkspaceName() {
    return selectedWorkspaceName;
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
   * Given a workspace name, this method returns the associated workspace.
   * 
   * @param workspaceName
   *          the name of the workspace.
   * @return the selected workspace.
   */
  public Workspace getWorkspace(String workspaceName) {
    if (workspaces != null) {
      return workspaces.get(workspaceName);
    }
    return null;
  }

  /**
   * Returns the list of workspace names. This list defines the set of
   * workspaces the user has access to.
   * 
   * @return the list of workspace names.
   */
  public List<String> getWorkspaceNames() {
    if (workspaces != null) {
      List<String> workspaceNames = new ArrayList<String>();
      for (Map.Entry<String, Workspace> wsEntry : workspaces.entrySet()) {
        if (isAccessGranted(wsEntry.getValue())) {
          workspaceNames.add(wsEntry.getKey());
        }
      }
      return workspaceNames;
    }
    return Collections.<String> emptyList();
  }

  /**
   * {@inheritDoc}
   */
  public void pinModule(Module module) {
    pinModule(getSelectedWorkspaceName(), module);
  }

  /**
   * Configures an application-wide action map that will be installed in the
   * main application frame. These actions are available at any time from the UI
   * and thus, do not depend on the active workspace. General purpose actions
   * like &quot;Change password&quot; action should be installed here.
   * 
   * @param actionMap
   *          the actionMap to set.
   */
  public void setActionMap(ActionMap actionMap) {
    this.actionMap = actionMap;
  }

  /**
   * Sets the application description i18n key. The way this description is
   * actully leveraged depends on the UI channel.
   * 
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    controllerDescriptor.setDescription(description);
  }

  /**
   * Configures the exit action to be executed whenever the user wants to quit
   * the application. The default installed exit action fisrt checks for started
   * module(s) dirty state(s), then notifies user of pending persistent changes.
   * When no flush is needed or the user bypasses them, the actual exit is
   * performed.
   * 
   * @param exitAction
   *          the exitAction to set.
   */
  public void setExitAction(IDisplayableAction exitAction) {
    this.exitAction = exitAction;
  }

  /**
   * Configures the locale used to initiate the login process. Whenever the
   * forced starting locale is <code>null</code>, the client host default locale
   * is used.
   * <p>
   * As soon as the user logs-in, his locale is then used to translate the UI.
   * Whenever the login process is disabled, then the forced starting locale is
   * kept as the UI i18n locale.
   * 
   * @param forcedStartingLocale
   *          the forcedStartingLocale to set.
   */
  public void setForcedStartingLocale(String forcedStartingLocale) {
    this.forcedStartingLocale = forcedStartingLocale;
  }

  /**
   * Configures the help action map. The help action map should contain actions
   * that are related to helping the user (online help, reference manual,
   * tutorial, version dialog...).
   * <p>
   * The help action map is visually distinguished from the regular aplication
   * action map. For instance elp actions can be represented in a menu that is
   * right-aligned in the menubar.
   * 
   * @param helpActionMap
   *          the helpActionMap to set.
   */
  public void setHelpActionMap(ActionMap helpActionMap) {
    this.helpActionMap = helpActionMap;
  }

  /**
   * Configures the navigation action map. The navigation action map should
   * contain actions that are related to navigating the modules and workspace
   * history, e.g. previous, next, home, and so on.
   * 
   * @param navigationActionMap
   *          the navigationActionMap to set.
   */
  public void setNavigationActionMap(ActionMap navigationActionMap) {
    this.navigationActionMap = navigationActionMap;
  }

  /**
   * Sets the icon image URL that is used as the application icon. Supported URL
   * protocols include :
   * <ul>
   * <li>all JVM supported protocols</li>
   * <li>the <b>jar:/</b> pseudo URL protocol</li>
   * <li>the <b>classpath:/</b> pseudo URL protocol</li>
   * </ul>
   * 
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    controllerDescriptor.setIconImageURL(iconImageURL);
  }

  /**
   * Configures the name of the JAAS login context to use to authenticate users.
   * It must reference a valid JAAS context that is installed in the JVM, either
   * through setting the <code>java.security.auth.login.config</code> system
   * property or through server-specific configuration.
   * 
   * @param loginContextName
   *          the loginContextName to set.
   */
  public void setLoginContextName(String loginContextName) {
    this.loginContextName = loginContextName;
  }

  /**
   * Configures the view descriptor used to create the login dialog. The default
   * built-in login view descriptor includes a standard login/password form.
   * 
   * @param loginViewDescriptor
   *          the loginViewDescriptor to set.
   */
  public void setLoginViewDescriptor(IViewDescriptor loginViewDescriptor) {
    this.loginViewDescriptor = loginViewDescriptor;
  }

  /**
   * Configures the MVC binder used to apply model-view bindings. There is
   * hardly any reason for the developper to change the default binder but it
   * can be customized here.
   * 
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Sets the application name i18n key. The way this nae is actully leveraged
   * depends on the UI channel but it typically generates (part of the) frame
   * title.
   * 
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    controllerDescriptor.setName(name);
  }

  /**
   * Configures an action to be executed each time a module of the application
   * is entered. The action is executed in the context of the module the user
   * enters.
   * 
   * @param onModuleEnterAction
   *          the onModuleEnterAction to set.
   */
  public void setOnModuleEnterAction(IAction onModuleEnterAction) {
    this.onModuleEnterAction = onModuleEnterAction;
  }

  /**
   * Configures an action to be executed each time a module of the application
   * is exited. The action is executed in the context of the module the user
   * exits. Default frontend controller configuration installs an action that
   * checks current module dirty state.
   * 
   * @param onModuleExitAction
   *          the onModuleExitAction to set.
   */
  public void setOnModuleExitAction(IAction onModuleExitAction) {
    this.onModuleExitAction = onModuleExitAction;
  }

  /**
   * Configures an action to be executed on an empty UI context when the
   * application starts. An example of such an action would be a default
   * workspace/module opening and selection, a &quot;tip of the day&quot; like
   * action, ...
   * 
   * @param startupAction
   *          the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
  }

  /**
   * Configures the view factory used to create views from view descriptors.
   * Using a custom view factory is typically needed for extending Jspresso to
   * use custom view descriptors / UI components. Of course, there is a view
   * factory contrete type per UI channel.
   * 
   * @param viewFactory
   *          the viewFactory to set.
   */
  public void setViewFactory(IViewFactory<E, F, G> viewFactory) {
    this.viewFactory = viewFactory;
  }

  /**
   * Configures the workspaces that are available in the application. Workspaces
   * are application entry-points and are hierarchically composed of modules /
   * sub-modules.
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
   * Sets the icon image URL that is used as the workspace menu icon. Supported
   * URL protocols include :
   * <ul>
   * <li>all JVM supported protocols</li>
   * <li>the <b>jar:/</b> pseudo URL protocol</li>
   * <li>the <b>classpath:/</b> pseudo URL protocol</li>
   * </ul>
   * 
   * @param workspacesMenuIconImageUrl
   *          the workspacesMenuIconImageUrl to set.
   */
  public void setWorkspacesMenuIconImageUrl(String workspacesMenuIconImageUrl) {
    this.workspacesMenuIconImageUrl = workspacesMenuIconImageUrl;
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
    selectedModules = new HashMap<String, Module>();
    workspaceNavigatorConnectors = new HashMap<String, ICompositeValueConnector>();
    workspaceViews = new HashMap<String, IMapView<E>>();
    backwardHistoryEntries = new LinkedList<ModuleHistoryEntry>();
    forwardHistoryEntries = new LinkedList<ModuleHistoryEntry>();
    selectedWorkspaceName = null;
    return getBackendController().stop();
  }

  /**
   * Creates a new login callback handler.
   * 
   * @return a new login callback handler
   */
  protected CallbackHandler createLoginCallbackHandler() {
    UsernamePasswordHandler uph = new UsernamePasswordHandler();
    String[] savedUserPass = decodeUserPass(getClientPreference(UP_KEY));
    if (savedUserPass != null && savedUserPass.length == 2
        && savedUserPass[0] != null) {
      uph.setUsername(savedUserPass[0]);
      uph.setPassword(savedUserPass[1]);
      uph.setRememberMe(true);
    } else {
      uph.setUsername(null);
      uph.setPassword(null);
      uph.setRememberMe(false);
    }
    return uph;
  }

  /**
   * Reads a client preference.
   * 
   * @param key
   *          the key under which the preference as been stored.
   * @return the stored preference or null.
   */
  public String getClientPreference(String key) {
    if (getClientPreferenceStore() != null) {
      return getClientPreferenceStore().getPreference(key);
    }
    return null;
  }

  /**
   * Stores a client preference.
   * 
   * @param key
   *          the key under which the preference as to be stored.
   * @param value
   *          the value of the preference to be stored.
   */
  public void putClientPreference(String key, String value) {
    if (getClientPreferenceStore() != null) {
      getClientPreferenceStore().putPreference(key, value);
    }
  }

  /**
   * Deletes a client preference.
   * 
   * @param key
   *          the key under which the preference is stored.
   */
  public void removeClientPreference(String key) {
    if (getClientPreferenceStore() != null) {
      getClientPreferenceStore().removePreference(key);
    }
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
   * Creates the module area view to display the modules content.
   * 
   * @param workspaceName
   *          the workspace to create the module area view for.
   * @return the the module area view to display the modules content.
   */
  protected IView<E> createModuleAreaView(String workspaceName) {
    IMapView<E> moduleAreaView = (IMapView<E>) viewFactory.createView(
        new WorkspaceCardViewDescriptor(), this, getLocale());
    workspaceViews.put(workspaceName, moduleAreaView);
    return moduleAreaView;
  }

  /**
   * Creates the workspace action list.
   * 
   * @return the workspace action list.
   */
  protected ActionList createWorkspaceActionList() {
    ActionList workspaceSelectionActionList = new ActionList();
    workspaceSelectionActionList.setName("workspaces");
    workspaceSelectionActionList
        .setIconImageURL(getWorkspacesMenuIconImageUrl());
    List<IDisplayableAction> workspaceSelectionActions = new ArrayList<IDisplayableAction>();
    for (String workspaceName : getWorkspaceNames()) {
      Workspace workspace = getWorkspace(workspaceName);
      if (isAccessGranted(workspace)) {
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
    }
    workspaceSelectionActionList.setActions(workspaceSelectionActions);
    workspaceSelectionActionList.setCollapsable(true);
    return workspaceSelectionActionList;
  }

  /**
   * Creates the workspace action map.
   * 
   * @return the workspace action map.
   */
  protected ActionMap createWorkspaceActionMap() {
    ActionMap workspaceActionMap = new ActionMap();
    List<ActionList> workspaceActionLists = new ArrayList<ActionList>();

    ActionList exitActionList = new ActionList();
    exitActionList.setName("file");
    exitActionList.setIconImageURL(getWorkspacesMenuIconImageUrl());
    List<IDisplayableAction> exitActions = new ArrayList<IDisplayableAction>();
    exitActions.add(getExitAction());
    exitActionList.setActions(exitActions);

    workspaceActionLists.add(createWorkspaceActionList());
    workspaceActionLists.add(exitActionList);
    workspaceActionMap.setActionLists(workspaceActionLists);

    return workspaceActionMap;
  }

  /**
   * Creates a workspace navigator based on the workspace definition.
   * 
   * @param workspaceName
   *          the workspace to create the navigator for.
   * @param workspaceNavigatorViewDescriptor
   *          the view descriptor of the navigator.
   * @return the workspace navigator view.
   */
  protected IView<E> createWorkspaceNavigator(final String workspaceName,
      IViewDescriptor workspaceNavigatorViewDescriptor) {
    IView<E> workspaceNavigatorView = viewFactory.createView(
        workspaceNavigatorViewDescriptor, this, getLocale());
    IItemSelectable workspaceNavigator;
    if (workspaceNavigatorView.getConnector() instanceof IItemSelectable) {
      workspaceNavigator = (IItemSelectable) workspaceNavigatorView
          .getConnector();
    } else {
      workspaceNavigator = (IItemSelectable) ((ICompositeValueConnector) workspaceNavigatorView
          .getConnector())
          .getChildConnector(ModelRefPropertyConnector.THIS_PROPERTY);
    }
    workspaceNavigator.addItemSelectionListener(new IItemSelectionListener() {

      public void selectedItemChange(ItemSelectionEvent event) {
        navigatorSelectionChanged(workspaceName,
            (ICompositeValueConnector) event.getSelectedItem());
      }
    });
    workspaceNavigatorConnectors.put(workspaceName,
        (ICompositeValueConnector) workspaceNavigatorView.getConnector());
    return workspaceNavigatorView;
  }

  /**
   * Must be called when a modal dialog is displayed.
   * 
   * @param context
   *          the context to store on the context stack.
   * @param reuseCurrent
   *          set to true to reuse an existing modal dialog.
   */
  protected void displayModalDialog(Map<String, Object> context,
      boolean reuseCurrent) {
    if (!reuseCurrent || dialogContextStack.size() == 0) {
      dialogContextStack.add(0, context);
    }
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
   * Creates the exit action.
   * 
   * @return the exit action.
   */
  public IDisplayableAction getExitAction() {
    if (exitAction == null) {
      ExitAction<E, F, G> action = new ExitAction<E, F, G>();
      action.setName("quit.name");
      action.setDescription("quit.description");
      action.setIconImageURL(getViewFactory().getIconFactory()
          .getCancelIconImageURL());
      exitAction = action;
    }
    return exitAction;
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
   * Gets the loginViewDescriptor.
   * 
   * @return the loginViewDescriptor.
   */
  protected IViewDescriptor getLoginViewDescriptor() {
    return loginViewDescriptor;
  }

  /**
   * Gets the onModuleEnterAction.
   * 
   * @return the onModuleEnterAction.
   */
  protected IAction getOnModuleEnterAction() {
    return onModuleEnterAction;
  }

  /**
   * Gets the onModuleExitAction.
   * 
   * @return the onModuleExitAction.
   */
  protected IAction getOnModuleExitAction() {
    return onModuleExitAction;
  }

  /**
   * Gets the selected module.
   * 
   * @param workspaceName
   *          the workspace name to query the selected module for.
   * @return the selected module.
   */
  protected Module getSelectedModule(String workspaceName) {
    return selectedModules.get(workspaceName);
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
   * {@inheritDoc}
   */
  public void loggedIn(Subject subject) {
    if (getLoginCallbackHandler() instanceof UsernamePasswordHandler) {
      UsernamePasswordHandler uph = (UsernamePasswordHandler) getLoginCallbackHandler();
      if (uph.isRememberMe()) {
        putClientPreference(UP_KEY,
            encodeUserPass(uph.getUsername(), uph.getPassword()));
      } else {
        removeClientPreference(UP_KEY);
      }
      uph.clear();
    }
    getBackendController().loggedIn(subject);
    if (workspaces != null) {
      for (Workspace workspace : workspaces.values()) {
        workspace.setSecurityHandler(this);
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
        loggedIn(lc.getSubject());
      } catch (LoginException le) {
        System.err.println("Authentication failed:");
        System.err.println("  " + le.getMessage());
        return false;
      }
    } else {
      loggedIn(getAnonymousSubject());
    }
    return true;
  }

  /**
   * Pins a module in the history navigation thus allowing the user to navigate
   * back.
   * 
   * @param workspaceName
   *          the workspace to pin the module for.
   * @param module
   *          the module to pin.
   */
  protected void pinModule(String workspaceName, Module module) {
    if (moduleAutoPinEnabled && module != null) {
      backwardHistoryEntries.add(new ModuleHistoryEntry(workspaceName, module));
      forwardHistoryEntries.clear();
    }
  }

  /**
   * Refines the data integrity violation exception to determine the translation
   * key from which the user message will be constructed.
   * 
   * @param exception
   *          the DataIntegrityViolationException.
   * @return the translation key to use.
   */
  protected String refineIntegrityViolationTranslationKey(
      DataIntegrityViolationException exception) {
    if (exception.getCause() instanceof ConstraintViolationException) {
      ConstraintViolationException cve = (ConstraintViolationException) exception
          .getCause();
      if (cve.getSQL() != null && cve.getSQL().toUpperCase().contains("DELETE")) {
        return "error.fk.delete";
      } else if (cve.getConstraintName() != null) {
        if (cve.getConstraintName().toUpperCase().contains("FK")) {
          return "error.fk.update";
        }
        return "error.unicity";
      }
      return "error.integrity";
    }
    return "error.integrity";
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

  private void navigatorSelectionChanged(String workspaceName,
      ICompositeValueConnector selectedConnector) {
    if (tracksWorkspaceNavigator) {
      if (selectedConnector != null
          && selectedConnector.getConnectorValue() instanceof Module) {
        Module selectedModule = (Module) selectedConnector.getConnectorValue();
        displayModule(workspaceName, selectedModule);
      } else {
        displayModule(workspaceName, null);
      }
    }
  }

  private Object[] synchWorkspaceNavigatorSelection(
      ICollectionConnectorListProvider navigatorConnector, Module module) {
    Object[] result = null;
    int moduleModelIndex = -1;
    for (ICollectionConnector childCollectionConnector : navigatorConnector
        .getCollectionConnectors()) {
      for (int i = 0; i < childCollectionConnector.getChildConnectorCount(); i++) {
        IValueConnector childConnector = childCollectionConnector
            .getChildConnector(i);
        if (module != null && module.equals(childConnector.getConnectorValue())) {
          moduleModelIndex = i;
        }
        if (childConnector instanceof ICollectionConnectorListProvider) {
          Object[] subResult = synchWorkspaceNavigatorSelection(
              (ICollectionConnectorListProvider) childConnector, module);
          if (subResult != null) {
            result = subResult;
          }
        }
      }
      if (moduleModelIndex >= 0) {
        result = new Object[] {
            childCollectionConnector, new Integer(moduleModelIndex)
        };
      } else {
        childCollectionConnector.setSelectedIndices(null, -1);
      }
    }
    return result;
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
   * Gets the frameWidth.
   * 
   * @return the frameWidth.
   */
  protected Integer getFrameWidth() {
    return frameWidth;
  }

  /**
   * Sets the preferred application frame width. How this dimension is leveraged
   * depends on the UI channel.
   * 
   * @param frameWidth
   *          the frameWidth to set.
   */
  public void setFrameWidth(Integer frameWidth) {
    this.frameWidth = frameWidth;
  }

  /**
   * Gets the frameHeight.
   * 
   * @return the frameHeight.
   */
  protected Integer getFrameHeight() {
    return frameHeight;
  }

  /**
   * Sets the preferred application frame height. How this dimension is
   * leveraged depends on the UI channel.
   * 
   * @param frameHeight
   *          the frameHeight to set.
   */
  public void setFrameHeight(Integer frameHeight) {
    this.frameHeight = frameHeight;
  }

  /**
   * Gets the secondaryActionMap.
   * 
   * @return the secondaryActionMap.
   */
  public ActionMap getSecondaryActionMap() {
    return secondaryActionMap;
  }

  /**
   * Assigns the view secondary action map. Same rules as the primary action map
   * apply except that actions in this map should be visually distinguished from
   * the main action map, e.g. placed in another toolbar.
   * 
   * @param secondaryActionMap
   *          the secondaryActionMap to set.
   */
  public void setSecondaryActionMap(ActionMap secondaryActionMap) {
    this.secondaryActionMap = secondaryActionMap;
  }

  /**
   * Encodes username / password into a string for storing. The stored string is
   * used later for "remember me" function.
   * 
   * @param username
   *          the user name.
   * @param password
   *          the user password;
   * @return the encoded username/password string.
   */
  protected String encodeUserPass(String username, String password) {
    StringBuffer buff = new StringBuffer();
    if (username != null) {
      buff.append(username);
    }
    buff.append(UP_SEP);
    if (password != null) {
      // buff.append(password);
    }
    return buff.toString();
  }

  /**
   * Decodes username / password from a string for restoring into original
   * values. This is used in "remember me" function.
   * 
   * @param encodedUserPass
   *          the encoded username/password string.
   * @return an string array of username/password strings
   */
  protected String[] decodeUserPass(String encodedUserPass) {
    String[] userPass = new String[2];
    if (encodedUserPass != null) {
      String[] temp = encodedUserPass.split(UP_SEP);
      if (temp.length == 2) {
        userPass[0] = temp[0];
        // userPass[1] = temp[1];
      } else if (temp.length == 1) {
        if (encodedUserPass.indexOf(UP_SEP) == 0) {
          // userPass[1] = temp[0];
        } else {
          userPass[0] = temp[0];
        }
      }
    }
    return userPass;
  }

  /**
   * {@inheritDoc}
   */
  public IView<E> getCurrentModuleView() {
    IMapView<E> workspaceView = workspaceViews.get(getSelectedWorkspaceName());
    if (workspaceView != null) {
      return workspaceView.getCurrentView();
    }
    return null;
  }

  /**
   * Gets the client preferences store.
   * 
   * @return the client preferences store.
   */
  protected synchronized IPreferencesStore getClientPreferenceStore() {
    if (clientPreferenceStore == null) {
      clientPreferenceStore = createClientPreferenceStore();
      clientPreferenceStore.setStorePath(new String[] {
        getName()
      });
    }
    return clientPreferenceStore;
  }

  /**
   * Creates the clientPreferenceStore.
   * 
   * @return the clientPreferenceStore.
   */
  protected abstract IPreferencesStore createClientPreferenceStore();

  /**
   * Sets the clientPreferenceStore.
   * 
   * @param clientPreferenceStore
   *          the clientPreferenceStore to set.
   */
  public void setClientPreferenceStore(IPreferencesStore clientPreferenceStore) {
    this.clientPreferenceStore = clientPreferenceStore;
  }

  /**
   * Delegates to the backend controller.
   * <p>
   * {@inheritDoc}
   */
  public String getUserPreference(String key) {
    return getBackendController().getUserPreference(key);
  }

  /**
   * {@inheritDoc}
   */
  public void putUserPreference(String key, String value) {
    getBackendController().putUserPreference(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public void removeUserPreference(String key) {
    getBackendController().removeUserPreference(key);
  }
}
