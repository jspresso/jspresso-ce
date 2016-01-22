/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.LocaleUtils;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.AbstractController;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.workspace.ExitAction;
import org.jspresso.framework.application.frontend.action.workspace.WorkspaceSelectionAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.security.SecurityContextConstants;
import org.jspresso.framework.application.view.descriptor.basic.WorkspaceCardViewDescriptor;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorListProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityContextBuilder;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.security.UsernamePasswordHandler;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.preferences.IPreferencesStore;
import org.jspresso.framework.util.uid.RandomGUID;
import org.jspresso.framework.util.url.UrlHelper;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;

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
   * {@code MAX_LOGIN_RETRIES}.
   */
  protected static final int    MAX_LOGIN_RETRIES = 3;
  private static final   Logger LOG               = LoggerFactory.getLogger(AbstractFrontendController.class);
  private static final   String UP_KEY            = "UP_KEY";
  private static final   String UP_SEP            = "!";
  private static final   String UP_GUID           = "UP_GUID";
  private static final   String LANG_KEY          = "LANG_KEY";
  private static final   String TZ_KEY            = "TZ_KEY";
  private static final   String CURR_DIALOG_VIEW  = "CURR_DIALOG_VIEW";
  private final List<ModuleHistoryEntry>              backwardHistoryEntries;
  private final DefaultIconDescriptor                 controllerDescriptor;
  private final List<Map<String, Object>>             dialogContextStack;
  private final List<ModuleHistoryEntry>              forwardHistoryEntries;
  private final Map<String, IMapView<E>>              workspaceViews;
  private final Map<String, Module>                   selectedModules;
  private final Map<String, ICompositeValueConnector> workspaceNavigatorConnectors;
  private       boolean                               started;
  private       ActionMap                             actionMap;
  private       ActionMap                             secondaryActionMap;
  private       Locale                                clientLocale;
  private       IDisplayableAction                    exitAction;
  private       String                                forcedStartingLocale;
  private       ActionMap                             helpActionMap;
  private       ActionMap                             navigationActionMap;
  private       UsernamePasswordHandler               loginCallbackHandler;
  private       String                                loginContextName;
  private       IViewDescriptor                       loginViewDescriptor;
  private       boolean                               moduleAutoPinEnabled;
  private       IMvcBinder                            mvcBinder;
  private       IAction                               onModuleEnterAction;
  private       IAction                               onModuleExitAction;
  private       IAction                               onModuleStartupAction;
  private       String                                selectedWorkspaceName;
  private       IAction                               loginAction;
  private       IAction                               startupAction;
  private       boolean                               tracksWorkspaceNavigator;
  private       IViewFactory<E, F, G>                 viewFactory;
  private       Map<String, Workspace>                workspaces;
  private       String                                workspacesMenuIconImageUrl;
  private       Integer                               frameWidth;
  private       Integer                               frameHeight;
  private       IPreferencesStore                     clientPreferencesStore;
  private       boolean                               checkActionThreadSafety;
  private final PropertyChangeListener                dirtInterceptor;

  /**
   * Constructs a new {@code AbstractFrontendController} instance.
   */
  public AbstractFrontendController() {
    started = false;
    controllerDescriptor = new DefaultIconDescriptor();
    selectedModules = new HashMap<>();
    dialogContextStack = new ArrayList<>();
    workspaceNavigatorConnectors = new HashMap<>();
    workspaceViews = new HashMap<>();
    backwardHistoryEntries = new LinkedList<>();
    forwardHistoryEntries = new LinkedList<>();
    moduleAutoPinEnabled = true;
    tracksWorkspaceNavigator = true;
    checkActionThreadSafety = true;
    dirtInterceptor = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        Module module = getSelectedModule();
        if (module != null && !module.isDirty()) {
          // Retrieve the top module
          while (module.getParent() != null) {
            module = module.getParent();
          }
          module.refreshDirtinessInDepth(getBackendController());
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayModule(Module module) {
    displayModule(getModuleWorkspace(module), module);
  }

  /**
   * Determines the workspace name of the parameter module.
   *
   * @param module
   *          the module to determine the workspace of.
   * @return the module workspace name. If no workspace already contains this
   *         module, defaults to the current one.
   */
  protected String getModuleWorkspace(Module module) {
    String selectedWsName = getSelectedWorkspaceName();
    // Look first in the current WS
    if (belongsTo(getWorkspace(selectedWsName), module)) {
      return selectedWsName;
    }
    // Then on the others
    for (String wsName : getWorkspaceNames()) {
      if (!wsName.equals(selectedWsName) && belongsTo(getWorkspace(wsName), module)) {
        return wsName;
      }
    }
    return selectedWsName;
  }

  private boolean belongsTo(Workspace owner, Module module) {
    for (Module child : owner.getModules()) {
      if (belongsTo(child, module)) {
        return true;
      }
    }
    return false;
  }

  private boolean belongsTo(Module owner, Module module) {
    if (owner == module) {
      return true;
    }
    List<Module> subModules = owner.getSubModules();
    if (subModules != null) {
      for (Module child : subModules) {
        if (belongsTo(child, module)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayModule(String workspaceName, Module module) {
    Module currentModule = getSelectedModule();
    // Test same workspace and same module. important when module is null and
    // selected module also to avoid stack overflows.
    if (((getSelectedWorkspaceName() == null && workspaceName == null) || ObjectUtils.equals(getSelectedWorkspaceName(),
        workspaceName)) && ((currentModule == null && module == null) || ObjectUtils.equals(currentModule, module))) {
      if (currentModule != null && module != null && ObjectUtils.equals(currentModule.getParent(),
          module.getParent())) {
        return;
      }
    }
    if (currentModule != null) {
      pinModule(getSelectedWorkspaceName(), currentModule);
      Map<String, Object> navigationContext = getModuleActionContext(getSelectedWorkspaceName());
      navigationContext.put(ActionContextConstants.FROM_MODULE, currentModule);
      navigationContext.put(ActionContextConstants.TO_MODULE, module);
      execute(currentModule.getExitAction(), navigationContext);
      execute(getOnModuleExitAction(), navigationContext);
    }
    displayWorkspace(workspaceName, true);
    IView<E> moduleAreaView = workspaceViews.get(workspaceName);
    if (moduleAreaView != null) {

      // The following does not seem necessary anymore.
      // This was done to cope with connectors mis-refreshing.

      // IValueConnector oldModuleModelConnector = moduleAreaViewConnector
      // .getModelConnector();
      // if (oldModuleModelConnector != null) {
      // oldModuleModelConnector.setConnectorValue(null);
      // }

      IValueConnector moduleModelConnector = getBackendController().getModuleConnector(module);
      mvcBinder.bind(moduleAreaView.getConnector(), moduleModelConnector);
    }
    selectedModules.put(workspaceName, module);
    if (module != null) {
      if (!module.isStarted()) {
        if (getOnModuleStartupAction() != null) {
          execute(getOnModuleStartupAction(), getModuleActionContext(workspaceName));
        }
        if (module.getStartupAction() != null) {
          execute(module.getStartupAction(), getModuleActionContext(workspaceName));
        }
      }
      module.setStarted(true);
      pinModule(getSelectedWorkspaceName(), module);
      Map<String, Object> navigationContext = getModuleActionContext(workspaceName);
      navigationContext.put(ActionContextConstants.FROM_MODULE, currentModule);
      navigationContext.put(ActionContextConstants.TO_MODULE, module);
      execute(getOnModuleEnterAction(), new HashMap<String, Object>());
      execute(module.getEntryAction(), new HashMap<String, Object>());
    }
    firePropertyChange(SELECTED_MODULE, currentModule, module);
    boolean wasTracksWorkspaceNavigator = tracksWorkspaceNavigator;
    try {
      tracksWorkspaceNavigator = false;
      ICompositeValueConnector workspaceNavigatorConnector = workspaceNavigatorConnectors.get(workspaceName);
      if (workspaceNavigatorConnector instanceof ICollectionConnectorListProvider) {
        Object[] result = synchWorkspaceNavigatorSelection(
            (ICollectionConnectorListProvider) workspaceNavigatorConnector, module);
        if (result != null) {
          int moduleModelIndex = (Integer) result[1];
          ((ICollectionConnector) result[0]).setSelectedIndices(new int[] {
            moduleModelIndex
          }, moduleModelIndex);
        }
      }
    } finally {
      tracksWorkspaceNavigator = wasTracksWorkspaceNavigator;
    }
  }

  private Map<String, Object> getModuleActionContext(String workspaceName) {
    IMapView<E> moduleAreaView = workspaceViews.get(workspaceName);
    if (moduleAreaView != null) {
      IView<E> moduleView = moduleAreaView.getCurrentView();
      if (moduleView != null) {
        return getViewFactory().getActionFactory().createActionContext(this,
            moduleView, moduleView.getConnector(), null, null);
      }
    }
    return new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
                  getSelectedModule())) {
            displayNextPinnedModule();
          } else {
            displayModule(nextWorkspaceName, nextModule);
            pinnedModuleDisplayed(nextEntry, false);
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
  @Override
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
                  getSelectedModule())) {
            displayPreviousPinnedModule();
          } else {
            displayModule(previousWorkspaceName, previousModule);
            pinnedModuleDisplayed(previousEntry, false);
          }
        } else {
          displayPreviousPinnedModule();
        }
      }
    } finally {
      moduleAutoPinEnabled = wasAutoPinEnabled;
    }
  }

  private String lastDisplayedSnapshotId;

  /**
   * Retrieves a pinned module in the backward or forward history and pins it.
   *
   * @param snapshotId
   *          the snapshot id of the module history to display.
   * @return the history entry actually displayed or null if no change.
   */
  protected ModuleHistoryEntry displayPinnedModule(String snapshotId) {
    if (!ObjectUtils.equals(snapshotId, lastDisplayedSnapshotId)) {
      lastDisplayedSnapshotId = snapshotId;
      ModuleHistoryEntry historyEntryToDisplay = null;
      for (ModuleHistoryEntry historyEntry : backwardHistoryEntries) {
        if (snapshotId.equals(historyEntry.getId())) {
          historyEntryToDisplay = historyEntry;
        }
      }
      if (historyEntryToDisplay != null) {
        while (backwardHistoryEntries.contains(historyEntryToDisplay)) {
          displayPreviousPinnedModule();
        }
        return historyEntryToDisplay;
      }
      for (ModuleHistoryEntry historyEntry : forwardHistoryEntries) {
        if (snapshotId.equals(historyEntry.getId())) {
          historyEntryToDisplay = historyEntry;
        }
      }
      if (historyEntryToDisplay != null) {
        while (forwardHistoryEntries.contains(historyEntryToDisplay)) {
          displayNextPinnedModule();
        }
        return historyEntryToDisplay;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void displayWorkspace(String workspaceName) {
    displayWorkspace(workspaceName, false);
  }

  /**
   * Displays a workspace.
   *
   * @param workspaceName
   *          the workspace identifier.
   * @param bypassModuleBoundaryActions
   *          should we bypass module onEnter/Exit actions ?
   */
  protected void displayWorkspace(String workspaceName,
      boolean bypassModuleBoundaryActions) {
    if ((getSelectedWorkspaceName() == null && workspaceName == null)
        || ObjectUtils.equals(getSelectedWorkspaceName(), workspaceName)) {
      return;
    }
    Workspace workspace = null;
    boolean startingWorkspace = false;
    if (workspaceName != null) {
      workspace = getWorkspace(workspaceName);
      if (workspace != null) {
        startingWorkspace = !workspace.isStarted();
        if (startingWorkspace) {
          workspace.setStarted(true);
        }
      }
    }
    if (bypassModuleBoundaryActions) {
      Workspace oldSelectedWorkspace = getSelectedWorkspace();
      this.selectedWorkspaceName = workspaceName;
      firePropertyChange(SELECTED_WORKSPACE, oldSelectedWorkspace, getSelectedWorkspace());
    } else {
      // do as if we had selected the module in the target workspace.
      // so that module boundary actions get triggered
      // see bug #538
      displayModule(workspaceName, getSelectedModule(workspaceName));
    }
    // Delay until the end of the very 1st execution. See bug #42.
    if (workspace != null && startingWorkspace && workspace.getStartupAction() != null) {
      Map<String, Object> actionContext = getInitialActionContext();
      actionContext.put(ActionContextConstants.ACTION_PARAM, workspace);
      execute(workspace.getStartupAction(), actionContext);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean disposeModalDialog(E sourceWidget, Map<String, Object> context) {
    LOG.debug("Disposing modal dialog.");
    if (dialogContextStack.size() > 0) {
      Map<String, Object> savedContext = dialogContextStack.remove(0);
      E currentDialogView = (E) savedContext.get(CURR_DIALOG_VIEW);

      if (currentDialogView != null && !isParentOf(currentDialogView,
          (IView<E>) context.get(ActionContextConstants.VIEW))) {
        dialogContextStack.add(0, savedContext);
        LOG.debug("Trying to dispose a dialog that is not the top one. Ignoring.");
        return false;
      }
      // preserve action param
      Object actionParam = context.get(ActionContextConstants.ACTION_PARAM);
      context.putAll(savedContext);
      context.put(ActionContextConstants.ACTION_PARAM, actionParam);
      return true;
    } else {
      LOG.debug("Trying to dispose a modal dialog while there is no dialog left.");
    }
    return false;
  }

  /**
   * Transfer focus.
   *
   * @param context the context
   */
  protected void transferFocus(Map<String, Object> context) {
    @SuppressWarnings("unchecked")
    E componentToFocus = (E) context.get(FrontendAction.COMPONENT_TO_FOCUS);
    if (componentToFocus != null) {
      focus(componentToFocus);
    }
  }

  private boolean isParentOf(E parentView, IView<E> view) {
    if (view == null) {
      return false;
    }
    if (parentView == view.getPeer()) {
      return true;
    }
    return isParentOf(parentView, view.getParent());
  }

  private boolean executeDelayedActions       = true;
  private boolean checkActionChainTheadSafety = true;

  /**
   * Executes frontend actions and delegates backend actions execution to its
   * peer backend controller.
   * <p/>
   * {@inheritDoc}
   */
  @SuppressWarnings({"ThrowFromFinallyBlock", "ConstantConditions" })
  @Override
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    Map<String, Object> actionContext = getInitialActionContext();
    // Retain only entries from the initial action context that are not in the
    // action context.
    actionContext.putAll(context);
    context.putAll(actionContext);
    // This is handled here since the selected module might have changed during
    // the action chain.
    context.put(ActionContextConstants.CURRENT_MODULE, getSelectedModule());
    boolean result;
    Map<String, Object> initialActionState = null;
    boolean savedExecuteDelayedActions = executeDelayedActions;
    boolean savedCheckActionChainTheadSafety = checkActionChainTheadSafety;
    try {
      if (executeDelayedActions) {
        executeDelayedActions = false;
        executeDelayedActions(this);
      }
      if (isCheckActionThreadSafety() && checkActionChainTheadSafety) {
        checkActionChainTheadSafety = false;
        try {
          initialActionState = extractInternalActionState(action);
        } catch (IllegalAccessException ex) {
          throw new ActionException(ex,
              "Unable to extract internal action state for thread-safety checking of action : " + action);
        }
      }
      // Should be handled before getting there.
      // checkAccess(action);
      if (action.isBackend()) {
        result = executeBackend(action, context);
      } else {
        result = executeFrontend(action, context);
      }
    } catch (Throwable ex) {
      Throwable refinedException = ex;
      if (ex instanceof HibernateException) {
        refinedException = SessionFactoryUtils.convertHibernateAccessException((HibernateException) ex);
      }
      handleException(refinedException, context);
      result = false;
    } finally {
      executeDelayedActions = savedExecuteDelayedActions;
      checkActionChainTheadSafety = savedCheckActionChainTheadSafety;
      if (initialActionState != null) {
        Map<String, Object> finalActionState;
        try {
          finalActionState = extractInternalActionState(action);
        } catch (IllegalAccessException ex) {
          throw new ActionException(ex,
              "Unable to extract internal action state for thread-safety checking of action : " + action);
        }
        if (!initialActionState.equals(finalActionState)) {
          LOG.error("A coding problem has been detected that breaks action thread-safety.\n"
              + "The action internal state has been modified during its execution which is strictly forbidden.\n"
              + "The action chain started with : {}", action);
          logInternalStateDifferences("root", initialActionState, finalActionState);
          throw new ActionException("A coding problem has been detected that breaks action thread-safety.\n"
              + "The action internal state has been modified during its execution which is strictly forbidden.\n"
              + "The action chain started with : " + action);
        }
      }
    }
    return result;
  }

  /**
   * Execute delayed actions.
   *
   * @param actionHandler the action handler
   */
  @Override
  public void executeDelayedActions(IActionHandler actionHandler) {
    ((AbstractController) getBackendController()).executeDelayedActions(this);
    super.executeDelayedActions(actionHandler);
  }

  @SuppressWarnings("unchecked")
  private void logInternalStateDifferences(String prefix, Map<String, Object> initialActionState,
                                           Map<String, Object> finalActionState) {
    for (Map.Entry<String, Object> initialEntry : initialActionState.entrySet()) {
      String leaf = initialEntry.getKey();
      if (leaf.indexOf('.') >= 0) {
        leaf = leaf.substring(leaf.lastIndexOf('.') + 1);
      }
      String path = prefix + "|" + leaf;
      if (finalActionState.containsKey(initialEntry.getKey())) {
        Object initialValue = initialEntry.getValue();
        Object finalValue = finalActionState.get(initialEntry.getKey());
        if (initialValue != null && finalValue == null) {
          LOG.error(">> [{}] is not null in the initial action state but null in the final one.", path);
        } else if (initialValue == null && finalValue != null) {
          LOG.error(">> [{}] is null in the initial action state but not null in the final one.", path);
        } else if (initialValue != null && !initialValue.equals(finalValue)) {
          if (initialValue instanceof Map<?, ?> && finalValue instanceof Map<?, ?>) {
            logInternalStateDifferences(path, (Map<String, Object>) initialValue, (Map<String, Object>) finalValue);
          } else {
            LOG.error(">> [{}] is different in the initial action state and in the final one.", path);
          }
        }
      } else {
        LOG.error(">> [{}] is present in the final action state but not in the initial one.", path);
      }
    }
    for (Map.Entry<String, Object> finalEntry : finalActionState.entrySet()) {
      if (!initialActionState.containsKey(finalEntry.getKey())) {
        String leaf = finalEntry.getKey();
        if (leaf.indexOf('.') >= 0) {
          leaf = leaf.substring(leaf.lastIndexOf('.') + 1);
        }
        String path = prefix + "|" + leaf;
        LOG.error(">> [{}] is present in the initial action state but not in the final one.", path);
      }
    }
  }

  /**
   * Gets the actions.
   *
   * @return the actions.
   */
  @Override
  public ActionMap getActionMap() {
    return actionMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IApplicationSession getApplicationSession() {
    return getBackendController().getApplicationSession();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IBackendController getBackendController() {
    return BackendControllerHolder.getCurrentBackendController();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return controllerDescriptor.getDescription();
  }

  /**
   * Gets the help actions.
   *
   * @return the help actions.
   */
  @Override
  public ActionMap getHelpActions() {
    return helpActionMap;
  }

  /**
   * Gets the navigation actions.
   *
   * @return the navigation actions.
   */
  @Override
  public ActionMap getNavigationActions() {
    return navigationActionMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider, Locale locale) {
    return controllerDescriptor.getI18nDescription(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getI18nName(ITranslationProvider translationProvider, Locale locale) {
    return controllerDescriptor.getI18nName(translationProvider, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return controllerDescriptor.getIcon();
  }

  /**
   * Contains nothing.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<>();
    initialActionContext.putAll(getBackendController().getInitialActionContext());
    for (int i = dialogContextStack.size() - 1; i >= 0; i--) {
      initialActionContext.putAll(dialogContextStack.get(i));
    }
    initialActionContext.put(ActionContextConstants.FRONT_CONTROLLER, this);
    initialActionContext.put(ActionContextConstants.MODULE, selectedModules.get(getSelectedWorkspaceName()));
    return initialActionContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getLocale() {
    if (getBackendController() != null) {
      Locale sessionLocale = getBackendController().getApplicationSession().getLocale();
      if (sessionLocale != null) {
        return sessionLocale;
      }
    }
    if (getForcedStartingLocale() != null) {
      return LocaleUtils.toLocale(getForcedStartingLocale());
    }
    return clientLocale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimeZone getClientTimeZone() {
    if (getBackendController() != null) {
      return getBackendController().getClientTimeZone();
    }
    return TimeZone.getDefault();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimeZone getReferenceTimeZone() {
    if (getBackendController() != null) {
      return getBackendController().getReferenceTimeZone();
    }
    return TimeZone.getDefault();
  }

  /**
   * Gets the mvcBinder.
   *
   * @return the mvcBinder.
   */
  @Override
  public IMvcBinder getMvcBinder() {
    return mvcBinder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return controllerDescriptor.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLastUpdated() {
    return controllerDescriptor.getLastUpdated();
  }

  /**
   * Gets the selectedWorkspaceName.
   *
   * @return the selectedWorkspaceName.
   */
  @Override
  public String getSelectedWorkspaceName() {
    return selectedWorkspaceName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getSelectedWorkspace() {
    return getWorkspace(getSelectedWorkspaceName());
  }

  @Override
  public Module getSelectedModule() {
    return getSelectedModule(getSelectedWorkspaceName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getLoginAction() {
    return loginAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAction getStartupAction() {
    return startupAction;
  }

  /**
   * Gets the viewFactory.
   *
   * @return the viewFactory.
   */
  @Override
  public IViewFactory<E, F, G> getViewFactory() {
    return viewFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace(String workspaceName) {
    return getWorkspace(workspaceName, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace(String workspaceName, boolean bypassSecurity) {
    if (workspaceName != null && workspaces != null) {
      Workspace workspace = workspaces.get(workspaceName);
      if (bypassSecurity || workspaceName.equals(getSelectedWorkspaceName()) || isAccessGranted(workspace)) {
        try {
          pushToSecurityContext(workspace);
          return workspace;
        } finally {
          restoreLastSecurityContextSnapshot();
        }
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getWorkspaceNames() {
    return getWorkspaceNames(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getWorkspaceNames(boolean bypassSecurity) {
    if (workspaces != null) {
      List<String> workspaceNames = new ArrayList<>();
      for (Map.Entry<String, Workspace> wsEntry : workspaces.entrySet()) {
        Workspace workspace = wsEntry.getValue();
        if (bypassSecurity || isAccessGranted(workspace)) {
          try {
            pushToSecurityContext(workspace);
            workspaceNames.add(wsEntry.getKey());
          } finally {
            restoreLastSecurityContextSnapshot();
          }
        }
      }
      return workspaceNames;
    }
    return Collections.emptyList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
   * actually leveraged depends on the UI channel.
   *
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    controllerDescriptor.setDescription(description);
  }

  /**
   * Configures the exit action to be executed whenever the user wants to quit
   * the application. The default installed exit action first checks for started
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
   * forced starting locale is {@code null}, the client host default locale
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
   * The help action map is visually distinguished from the regular application
   * action map. For instance elp actions can be represented in a menu that is
   * right-aligned in the menu bar.
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
   * through setting the {@code java.security.auth.login.config} system
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
   * hardly any reason for the developer to change the default binder but it
   * can be customized here.
   *
   * @param mvcBinder
   *          the mvcBinder to set.
   */
  public void setMvcBinder(IMvcBinder mvcBinder) {
    this.mvcBinder = mvcBinder;
  }

  /**
   * Sets the application name i18n key. The way this name is actually leveraged
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
   * Sets the lastUpdated.
   *
   * @param lastUpdated
   *          the lastUpdated to set.
   * @internal
   */
  public void setLastUpdated(long lastUpdated) {
    controllerDescriptor.setLastUpdated(lastUpdated);
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
   * is started. The action is executed in the context of the module the user
   * starts.
   *
   * @param onModuleStartupAction
   *          the onModuleStartupAction to set.
   */
  public void setOnModuleStartupAction(IAction onModuleStartupAction) {
    this.onModuleStartupAction = onModuleStartupAction;
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
   * Configures an action to be executed just after the user has successfully
   * logged-in but before any UI initialization has begun. An example of such an
   * action would be constructing a map of dynamic user right based on some
   * arbitrary data store so that the UI construction can actually depend on
   * these extracted values.
   *
   * @param loginAction
   *          the loginAction to set.
   */
  public void setLoginAction(IAction loginAction) {
    this.loginAction = loginAction;
  }

  /**
   * Configures an action to be executed on an empty UI context when the
   * application starts. The action executes once the user has logged-in and the
   * main UI has been constructed based on its access rights.An example of such
   * an action would be a default workspace/module opening and selection, a
   * &quot;tip of the day&quot; like action, ...
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
   * factory concrete type per UI channel.
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
    this.workspaces = new LinkedHashMap<>();
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
  @Override
  public boolean start(IBackendController peerController, Locale theClientLocale, TimeZone theClientTimeZone) {
    this.clientLocale = theClientLocale;
    Locale initialLocale = theClientLocale;
    if (forcedStartingLocale != null) {
      initialLocale = LocaleUtils.toLocale(forcedStartingLocale);
    }
    started = peerController.start(initialLocale, theClientTimeZone);
    peerController.addDirtInterceptor(dirtInterceptor);
    BackendControllerHolder.setSessionBackendController(peerController);
    return started;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isStarted() {
    return started;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (getApplicationSession().getPrincipal() != null) {
      LOG.info("User {} logged out for session {}.", getApplicationSession()
          .getUsername(), getApplicationSession().getId());
    }
    selectedModules.clear();
    workspaceNavigatorConnectors.clear();
    workspaceViews.clear();
    backwardHistoryEntries.clear();
    forwardHistoryEntries.clear();
    dialogContextStack.clear();

    selectedWorkspaceName = null;
    loginCallbackHandler = null;
    getBackendController().removeDirtInterceptor(dirtInterceptor);
    started = !getBackendController().stop();
    clearImplicitLogin();
    return !started;
  }

  /**
   * Clear implicit principal, i.e. SSO principal or "remember me" login.
   */
  protected void clearImplicitLogin() {
    String username = getApplicationSession().getUsername();
    if (!SecurityHelper.ANONYMOUS_USER_NAME.equals(username)) {
      if (getClientPreference(UP_KEY) != null) {
        // reset get through pass
        rememberLogin(username, null);
      }
    } else {
      UsernamePasswordHandler uph = getLoginCallbackHandler();
      uph.clear();
    }
    removeUserPreference(UP_GUID);
  }

  /**
   * Remember login.
   *
   * @param username the username
   * @param password the password
   */
  @Override
  public void rememberLogin(String username, String password) {
    putClientPreference(UP_KEY, encodeUserPass(username, password));
  }

  /**
   * Gets remembered login.
   *
   * @return the remembered login
   */
  @Override
  public String getRememberedLogin() {
    String[] savedUserPass = decodeUserPass(getClientPreference(UP_KEY));
    if (savedUserPass != null && savedUserPass.length > 0) {
      return savedUserPass[0];
    }
    return null;
  }

  /**
   * Creates a new login callback handler.
   *
   * @return a new login callback handler
   */
  protected UsernamePasswordHandler createLoginCallbackHandler() {
    UsernamePasswordHandler uph = createUsernamePasswordHandler();
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
    String savedLang = getClientPreference(LANG_KEY);
    if (savedLang != null && !savedLang.isEmpty()) {
      uph.setLanguage(savedLang);
    } else {
      uph.setLanguage(null);
    }
    String savedTzId = getClientPreference(TZ_KEY);
    if (savedTzId != null && !savedTzId.isEmpty()) {
      uph.setTimeZoneId(savedTzId);
    } else {
      uph.setTimeZoneId(null);
    }
    return uph;
  }

  /**
   * Creates a UsernamePassword handler instance.
   *
   * @return a new UsernamePassword handler instance.
   */
  protected UsernamePasswordHandler createUsernamePasswordHandler() {
    UsernamePasswordHandler uph = new UsernamePasswordHandler();
    return uph;
  }

  /**
   * Reads a client preference.
   *
   * @param key
   *          the key under which the preference as been stored.
   * @return the stored preference or null.
   */
  @Override
  public String getClientPreference(String key) {
    if (getClientPreferencesStore() != null) {
      return getClientPreferencesStore().getPreference(key);
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
  @Override
  public void putClientPreference(String key, String value) {
    if (getClientPreferencesStore() != null) {
      getClientPreferencesStore().putPreference(key, value);
    }
  }

  /**
   * Deletes a client preference.
   *
   * @param key
   *          the key under which the preference is stored.
   */
  @Override
  public void removeClientPreference(String key) {
    if (getClientPreferencesStore() != null) {
      getClientPreferencesStore().removePreference(key);
    }
  }

  /**
   * Creates and binds the login view.
   *
   * @return the login view
   */
  protected IView<E> createLoginView() {
    BasicViewDescriptor refinedViewDescriptor = ((BasicViewDescriptor) getLoginViewDescriptor()).clone();
    refinedViewDescriptor.setActionMap(null);
    refinedViewDescriptor.setSecondaryActionMap(null);
    IView<E> loginView = getViewFactory().createView(refinedViewDescriptor,
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
    List<IDisplayableAction> workspaceSelectionActions = new ArrayList<>();
    for (String workspaceName : getWorkspaceNames()) {
      Workspace workspace = getWorkspace(workspaceName);
      if (isAccessGranted(workspace)) {
        try {
          pushToSecurityContext(workspace);
          WorkspaceSelectionAction<E, F, G> workspaceSelectionAction = new WorkspaceSelectionAction<>();
          IViewDescriptor workspaceViewDescriptor = getWorkspace(workspaceName)
              .getViewDescriptor();
          workspaceSelectionAction.setWorkspaceName(workspaceName);
          workspaceSelectionAction.setName(workspaceViewDescriptor.getName());
          workspaceSelectionAction.setDescription(workspaceViewDescriptor
              .getDescription());
          workspaceSelectionAction.setIcon(workspaceViewDescriptor.getIcon());
          workspaceSelectionActions.add(workspaceSelectionAction);
        } finally {
          restoreLastSecurityContextSnapshot();
        }
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
    List<ActionList> workspaceActionLists = new ArrayList<>();

    ActionList exitActionList = new ActionList();
    exitActionList.setName("file");
    exitActionList.setIconImageURL(getWorkspacesMenuIconImageUrl());
    List<IDisplayableAction> exitActions = new ArrayList<>();
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

      @Override
      public void selectedItemChange(ItemSelectionEvent event) {
        navigatorSelectionChanged(workspaceName, (ICompositeValueConnector) event.getSelectedItem());
      }
    });
    workspaceNavigatorConnectors.put(workspaceName, (ICompositeValueConnector) workspaceNavigatorView.getConnector());
    return workspaceNavigatorView;
  }

  /**
   * Must be called when a modal dialog is displayed.
   *
   * @param dialogView
   *     the dialog view
   * @param context
   *     the context to store on the context stack.
   * @param reuseCurrent
   *     set to true to reuse an existing modal dialog.
   */
  protected void displayModalDialog(E dialogView, Map<String, Object> context,
      boolean reuseCurrent) {
    if (reuseCurrent && dialogContextStack.size() >= 1) {
      dialogContextStack.remove(0);
    }
    LOG.debug("Popping-up modal dialog.");
    context.put(CURR_DIALOG_VIEW, dialogView);
    dialogContextStack.add(0, context);
  }

  /**
   * Executes a backend action.
   *
   * @param action
   *          the backend action to execute.
   * @param context
   *          the action execution context.
   * @return true if the action was successfully executed.
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
   * @return true if the action was successfully executed.
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
  @Override
  public IDisplayableAction getExitAction() {
    if (exitAction == null) {
      ExitAction<E, F, G> action = new ExitAction<>();
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
  protected UsernamePasswordHandler getLoginCallbackHandler() {
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
   * Should a login dialog be displayed or should we process login implicitly
   * (either through SSO or using an anonymous subject in case of un-protected
   * application).
   *
   * @return true if {@code getLoginContext()} returns null.
   */
  protected boolean isLoginInteractive() {
    return getLoginContextName() != null && !shouldAutoLogin();
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
   * Gets the onModuleStartupAction.
   *
   * @return the onModuleStartupAction.
   */
  protected IAction getOnModuleStartupAction() {
    return onModuleStartupAction;
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
  @Override
  public void loggedIn(Subject subject) {
    UsernamePasswordHandler uph = getLoginCallbackHandler();
    if (!SecurityHelper.ANONYMOUS_USER_NAME.equals(uph.getUsername())) {
      if (uph.isRememberMe()) {
        rememberLogin(uph.getUsername(), uph.getPassword());
      } else {
        removeClientPreference(UP_KEY);
      }
    }
    String loginLocale = uph.getLanguage();
    if (loginLocale != null && !loginLocale.isEmpty()) {
      putClientPreference(LANG_KEY, loginLocale);
    } else {
      removeClientPreference(LANG_KEY);
    }
    String loginTimeZoneId = uph.getTimeZoneId();
    if (loginTimeZoneId != null && !loginTimeZoneId.isEmpty()) {
      putClientPreference(TZ_KEY, loginTimeZoneId);
    } else {
      removeClientPreference(TZ_KEY);
    }
    uph.clear();
    if (loginLocale != null) {
      for (Principal principal : subject.getPrincipals()) {
        if (principal instanceof UserPrincipal) {
          ((UserPrincipal) principal).putCustomProperty(UserPrincipal.LANGUAGE_PROPERTY, loginLocale);
        }
      }
    }
    if (loginTimeZoneId != null) {
      TimeZone loginTimeZone = TimeZone.getTimeZone(loginTimeZoneId);
      if (loginTimeZone != null) {
        getBackendController().setClientTimeZone(loginTimeZone);
      }
    }
    getBackendController().loggedIn(subject);
    execute(getLoginAction(), getLoginActionContext());
    if (workspaces != null) {
      Map<String, Workspace> filteredWorkspaces = new HashMap<>();
      for (Map.Entry<String, Workspace> workspaceEntry : workspaces.entrySet()) {
        Workspace workspace = workspaceEntry.getValue();
        // Must be put here so that ws that are not accessible
        // due to security restrictions are still translated.
        translateWorkspace(workspace);
        if (isAccessGranted(workspace)) {
          try {
            pushToSecurityContext(workspace);
            workspace.setSecurityHandler(this);
            translateWorkspace(workspace);
            filteredWorkspaces.put(workspaceEntry.getKey(),
                workspaceEntry.getValue());
          } finally {
            restoreLastSecurityContextSnapshot();
          }
        }
      }
      getBackendController().installWorkspaces(filteredWorkspaces);
    }
  }

  /**
   * Constructs the context to call the login action. Defaults to
   * {@link AbstractFrontendController#getInitialActionContext()}.
   *
   * @return the login action context.
   */
  protected Map<String, Object> getLoginActionContext() {
    return getInitialActionContext();
  }

  /**
   * Constructs the context to call the startup action. Defaults to
   * {@link AbstractFrontendController#getInitialActionContext()}.
   *
   * @return the startup action context.
   */
  protected Map<String, Object> getStartupActionContext() {
    return getInitialActionContext();
  }

  /**
   * Request anonymous login to tha application.
   */
  @Override
  public void loginAnonymously() {
    UsernamePasswordHandler uph = getLoginCallbackHandler();
    uph.setUsername(SecurityHelper.ANONYMOUS_USER_NAME);
    uph.setPassword("");
    login();
  }

  /**
   * Perform JAAS login.
   *
   * @return the logged-in subject or null if login failed.
   */
  protected Subject performJAASLogin() {
    CallbackHandler lch = getLoginCallbackHandler();
    try {
      LoginContext lc;
      try {
        lc = new LoginContext(getLoginContextName(), lch);
      } catch (LoginException le) {
        LOG.error("Cannot create LoginContext.", le);
        return null;
      } catch (SecurityException se) {
        LOG.error("Cannot create LoginContext.", se);
        return null;
      }
      lc.login();
      return lc.getSubject();
    } catch (LoginException le) {
      // le.getCause() is always null, so cannot rely on it.
      // see bug #1019
      if (!(le instanceof FailedLoginException)) {
        String message = le.getMessage();
        if (message.indexOf(':') > 0) {
          String exceptionClassName = message.substring(0, message.indexOf(':'));
          try {
            if (Throwable.class.isAssignableFrom(Class.forName(exceptionClassName))) {
              LOG.error("A technical exception occurred on login module.", le);
            }
          } catch (ClassNotFoundException ignored) {
            // ignored.
          }
        }
      }
      return null;
    }
  }

  /**
   * Performs the actual JAAS login.
   *
   * @return true if login succeeded.
   */
  protected boolean performLogin() {
    Subject subject;
    String lcName = getLoginContextName();
    if (lcName != null) {
      subject = performJAASLogin();
    } else {
      subject = getAnonymousSubject();
    }
    if (subject == null) {
      LOG.info("User {} failed to log in for session {}.", getLoginCallbackHandler().getUsername(),
          getApplicationSession().getId());
      return false;
    }
    LOG.info("User {} logged in  for session {}.", getLoginCallbackHandler().getUsername(),
        getApplicationSession().getId());
    loggedIn(subject);
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
      if (backwardHistoryEntries.size() > 0) {
        ModuleHistoryEntry lastPinnedModule = backwardHistoryEntries
            .get(backwardHistoryEntries.size() - 1);
        if (lastPinnedModule.getWorkspaceName().equals(workspaceName)
            && lastPinnedModule.getModule().equals(module)) {
          return;
        }
      }
      String historyEntryName = getWorkspace(workspaceName).getI18nName()
          + " - " + module.getI18nName();
      ModuleHistoryEntry historyEntry = new ModuleHistoryEntry(workspaceName,
          module, historyEntryName);
      backwardHistoryEntries.add(historyEntry);
      pinnedModuleDisplayed(historyEntry, true);
      forwardHistoryEntries.clear();
    }
  }

  /**
   * Callback when a module is actually pinned in history.
   *
   * @param historyEntry           the pinned module history entry.
   * @param addToHistory the add to history
   */
  protected void pinnedModuleDisplayed(ModuleHistoryEntry historyEntry, boolean addToHistory) {
    // NO-OP. Managed by subclasses when needed.
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
      }
      if (cve.getConstraintName() != null) {
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
   * Computes a user friendly exception message if this exception is known and
   * can be cleanly handled by the framework.
   *
   * @param exception
   *          the exception to compute the message for.
   * @return the user friendly message or null if this exception is unexpected.
   */
  protected String computeUserFriendlyExceptionMessage(Throwable exception) {
    if (exception instanceof SecurityException) {
      return exception.getMessage();
    }
    if (exception instanceof BusinessException) {
      return ((BusinessException) exception).getI18nMessage(this, getLocale());
    }
    if (exception instanceof DataIntegrityViolationException) {
      String constraintTranslation = null;
      if (exception.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException cve = ((ConstraintViolationException) exception
            .getCause());
        if (cve.getConstraintName() != null) {
          constraintTranslation = getTranslation(cve.getConstraintName(),
              getLocale());
        }
      }
      if (constraintTranslation == null) {
        constraintTranslation = getTranslation("unknown", getLocale());
      }
      return getTranslation(
          refineIntegrityViolationTranslationKey((DataIntegrityViolationException) exception),
          new Object[] {
            constraintTranslation
          }, getLocale());
    }
    if (exception instanceof ConcurrencyFailureException) {
      return getTranslation("concurrency.error.description", getLocale());
    }
    return null;
  }

  private void navigatorSelectionChanged(String workspaceName,
      ICompositeValueConnector selectedConnector) {
    if (tracksWorkspaceNavigator) {
      handleWorkspaceNavigatorSelection(workspaceName, selectedConnector);
    }
  }

  /**
   * Handle workspace navigator selection.
   *
   * @param workspaceName the workspace name
   * @param selectedConnector the selected connector
   */
  protected void handleWorkspaceNavigatorSelection(String workspaceName, ICompositeValueConnector selectedConnector) {
    if (selectedConnector != null
        && selectedConnector.getConnectorValue() instanceof Module) {
      Module selectedModule = selectedConnector.getConnectorValue();
      displayModule(workspaceName, selectedModule);
      // We do not reset displayed module on navigator selection anymore.
      // This is because when a node is selected in the tree at different
      // level,
      // the module connector selection is a 2-step process :
      // 1. deselection
      // 2. selection
      // The problem is that you never have from and to modules
      // simultaneously,
      // thus preventing complex algorithms in onEnter/onLeave actions.
      // } else {
      // displayModule(workspaceName, null);
    }
  }

  @SuppressWarnings("ConstantConditions")
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
            childCollectionConnector, moduleModelIndex
        };
      } else {
        childCollectionConnector.setSelectedIndices(null, -1);
      }
    }
    return result;
  }

  private void translateModule(Module module) {
    module.setI18nName(getTranslation(module.getName(), getLocale()));
    module.setI18nDescription(getTranslation(module.getDescription(),
        getLocale()));
    if (module.getSubModules() != null) {
      for (Module subModule : module.getSubModules()) {
        translateModule(subModule);
      }
    }
  }

  /**
   * Translate workspace.
   *
   * @param workspace the workspace
   */
  protected void translateWorkspace(Workspace workspace) {
    workspace.setI18nName(getTranslation(workspace.getName(), getLocale()));
    workspace.setI18nDescription(getTranslation(workspace.getDescription(), "", getLocale()));
    workspace.setI18nHeaderDescription(getTranslation(workspace.getHeaderDescription(), "", getLocale()));
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
  @Override
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
   * Should auto login.
   *
   * @return the boolean
   */
  protected boolean shouldAutoLogin() {
    UsernamePasswordHandler uph = getLoginCallbackHandler();
    return uph.getPassword() != null && uph.getPassword().length() == 0;
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
  @SuppressWarnings("UnusedParameters")
  protected String encodeUserPass(String username, String password) {
    String loginGuid = new RandomGUID().toString();
    StringBuilder buff = new StringBuilder();
    if (username != null) {
      buff.append(username);
    }
    buff.append(UP_SEP);
    if (password != null) {
      buff.append(loginGuid);
    }
    putUserPreference(getGlobalUserPreferenceGuidKey(username), loginGuid);
    return buff.toString();
  }

  private String getGlobalUserPreferenceGuidKey(String username) {
    // We must encode the username in the preference key, since the user is not yet logged-in,
    // thus the preference store is the global one.
    return UP_GUID + "|" + username;
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
        if (temp[1] != null && temp[1].equals(getUserPreference(getGlobalUserPreferenceGuidKey(userPass[0])))) {
          userPass[1] = "";
        }
      } else if (temp.length == 1) {
        if (encodedUserPass.indexOf(UP_SEP) != 0) {
          userPass[0] = temp[0];
        }
      }
    }
    return userPass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  protected synchronized IPreferencesStore getClientPreferencesStore() {
    if (clientPreferencesStore == null) {
      clientPreferencesStore = createClientPreferencesStore();
      clientPreferencesStore.setStorePath(getName());
    }
    return clientPreferencesStore;
  }

  /**
   * Creates the clientPreferenceStore.
   *
   * @return the clientPreferenceStore.
   */
  protected abstract IPreferencesStore createClientPreferencesStore();

  /**
   * Sets the clientPreferenceStore.
   *
   * @param clientPreferencesStore
   *          the clientPreferenceStore to set.
   */
  public void setClientPreferencesStore(IPreferencesStore clientPreferencesStore) {
    this.clientPreferencesStore = clientPreferencesStore;
  }

  /**
   * Delegates to the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getUserPreference(String key) {
    return getBackendController().getUserPreference(key);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putUserPreference(String key, String value) {
    getBackendController().putUserPreference(key, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUserPreference(String key) {
    getBackendController().removeUserPreference(key);
  }

  /**
   * Delegates to the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Locale locale) {
    return getBackendController().getTranslation(key, locale);
  }

  /**
   * Delegates to the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args, Locale locale) {
    return getBackendController().getTranslation(key, args, locale);
  }

  /**
   * Delegates to the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, String defaultMessage, Locale locale) {
    return getBackendController().getTranslation(key, defaultMessage, locale);
  }

  /**
   * Delegates to the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args,
      String defaultMessage, Locale locale) {
    return getBackendController().getTranslation(key, args, defaultMessage,
        locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAccessGranted(ISecurable securable) {
    Map<String, Object> currentSecurityContext = getSecurityContext();
    int snapshotsToRestore = 0;
    try {
      if (!currentSecurityContext
          .containsKey(SecurityContextConstants.WORKSPACE)) {
        pushToSecurityContext(getSelectedWorkspace());
        snapshotsToRestore++;
      }
      if (!currentSecurityContext
          .containsKey(SecurityContextConstants.MODULE_CHAIN)) {
        pushToSecurityContext(getSelectedModule());
        snapshotsToRestore++;
      }
      return getBackendController().isAccessGranted(securable);
    } finally {
      for (int i = 0; i < snapshotsToRestore; i++) {
        restoreLastSecurityContextSnapshot();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getSecurityContext() {
    return getBackendController().getSecurityContext();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISecurityContextBuilder pushToSecurityContext(Object contextElement) {
    getBackendController().pushToSecurityContext(contextElement);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISecurityContextBuilder restoreLastSecurityContextSnapshot() {
    getBackendController().restoreLastSecurityContextSnapshot();
    return this;
  }

  /**
   * Delegates to view factory.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void focus(E component) {
    getViewFactory().focus(component);
  }

  /**
   * Delegates to view factory.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void edit(E component) {
    getViewFactory().edit(component);
  }

  /**
   * Traces unexpected exceptions properly.
   *
   * @param ex
   *          the exception to trace.
   */
  @Override
  public void traceUnexpectedException(Throwable ex) {
    String sessionId = "[unknown session]";
    String userId = "[unknown user]";
    if (getApplicationSession() != null) {
      sessionId = getApplicationSession().getId();
      userId = getApplicationSession().getUsername();
    }
    LOG.error("An unexpected error occurred for user {} on session {}.", userId, sessionId, ex);
  }

  /**
   * Gets the checkActionThreadSafety.
   *
   * @return the checkActionThreadSafety.
   */
  public boolean isCheckActionThreadSafety() {
    return checkActionThreadSafety;
  }

  /**
   * Sets the checkActionThreadSafety.
   *
   * @param checkActionThreadSafety
   *          the checkActionThreadSafety to set.
   */
  public void setCheckActionThreadSafety(boolean checkActionThreadSafety) {
    this.checkActionThreadSafety = checkActionThreadSafety;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void displayModalDialog(E mainView, final List<G> actions,
      final String title, final E sourceComponent,
      final Map<String, Object> context, final Dimension dimension,
      boolean reuseCurrent) {
    displayDialog(mainView, actions, title, sourceComponent, context,
        dimension, reuseCurrent, true);
  }

  @Override
  public void displayUrl(String urlSpec) {
    displayUrl(urlSpec, UrlHelper.BLANK_TARGET);
  }
}
