/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionMonitoringPlugin;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.lov.LovAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.security.ApplicationDirectoryBuilder;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Base class of action monitoring plugins. It mainly stores the action call stack in the chain as well as start
 * timestamp and duration.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractActionMonitoringPlugin extends AbstractActionContextAware
    implements IActionMonitoringPlugin {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractActionMonitoringPlugin.class);

  private final static class ActionEntry {
    private IAction             action;
    private Map<String, Object> context;
    private Date                startTimestamp;

    /**
     * Instantiates a new Action entry.
     *
     * @param action
     *     the action
     * @param context
     *     the context
     * @param startTimestamp
     *     the start timestamp
     */
    public ActionEntry(IAction action, Map<String, Object> context, Date startTimestamp) {
      this.action = action;
      this.context = context;
      this.startTimestamp = startTimestamp;
    }
  }

  private class ActionsDirectoryBuilder extends ApplicationDirectoryBuilder {

    private Set<IDisplayableAction>         userActions;
    private Map<String, IDisplayableAction> lovActions;

    /**
     * Instantiates a new Actions directory builder.
     */
    public ActionsDirectoryBuilder() {
      userActions = new HashSet<>();
      lovActions = new HashMap<>();
    }

    /**
     * Process.
     *
     * @param componentDescriptor
     *     the component descriptor
     */
    @Override
    protected void process(IComponentDescriptor<?> componentDescriptor) {
      // Don't care about model stuff
    }

    /**
     * Process.
     *
     * @param propertyDescriptor
     *     the property descriptor
     * @param path
     *     the path
     * @param permIdsSet
     *     the perm ids set
     */
    @Override
    protected void process(IPropertyDescriptor propertyDescriptor, String path, Set<String> permIdsSet) {
      // Don't care about model stuff
    }

    /**
     * Process.
     *
     * @param action
     *     the action
     * @param path
     *     the path
     */
    @Override
    protected void process(IDisplayableAction action, String path) {
      if (!isEnabled() || !filter(action)) {
        return;
      }
      if (LovAction.class.equals(action.getClass())) {
        String referencePropertyName = path;
        if (referencePropertyName != null) {
          int lastDotIndex = referencePropertyName.lastIndexOf(".");
          if (lastDotIndex >= 0) {
            referencePropertyName = referencePropertyName.substring(lastDotIndex + 1);
          }
        }
        lovActions.put(referencePropertyName, action);
      } else {
        userActions.add(action);
      }
    }

    /**
     * Gets user actions.
     *
     * @return the user actions
     */
    public Set<IDisplayableAction> getUserActions() {
      return userActions;
    }

    public Map<String, IDisplayableAction> getLovActions() {
      return lovActions;
    }
  }

  private IApplicationSession currentSession;
  private UserPrincipal       currentUser;
  private Workspace           currentWorkspace;
  private Module              currentModule;
  private List<ActionEntry>   currentCallStack;
  private static boolean started = false;

  /**
   * Instantiates a new Abstract action monitoring plugin.
   */
  public AbstractActionMonitoringPlugin() {
    currentCallStack = new ArrayList<>();
  }

  /**
   * Trace results.
   *
   * @param session
   *     the session
   * @param user
   *     the current user
   * @param workspace
   *     the current workspace
   * @param module
   *     the current module
   * @param callStack
   *     the call stack
   * @param context
   *     the context
   * @param startTimestamp
   *     the start timestamp
   * @param endTimestamp
   *     the end timestamp
   */
  protected abstract void traceResults(IApplicationSession session, UserPrincipal user, Workspace workspace,
                                       Module module, List<IAction> callStack, Map<String, Object> context,
                                       Date startTimestamp, Date endTimestamp);

  /**
   * Trace user actions inventory.
   *
   * @param userActions
   *     the user actions
   * @param frontendController
   *     the frontend controller
   */
  protected abstract void traceUserActionsInventory(Set<IDisplayableAction> userActions,
                                                    Map<String, IDisplayableAction> lovActions,
                                                    IFrontendController<?, ?, ?> frontendController);

  /**
   * Action start.
   *
   * @param action
   *     the action
   * @param context
   *     the context
   */
  @Override
  public void actionStart(IAction action, Map<String, Object> context) {
    if (!isEnabled() || !filter(action)) {
      return;
    }
    Date startTimestamp = new Date();
    if (currentCallStack.isEmpty()) {
      IFrontendController<?, ?, ?> frontendController = getFrontendController(context);
      if (frontendController != null) {
        currentSession = frontendController.getApplicationSession();
        currentUser = currentSession.getPrincipal();
        currentWorkspace = frontendController.getSelectedWorkspace();
        currentModule = frontendController.getSelectedModule();
      } else {
        IBackendController backendController = getBackendController(context);
        currentSession = backendController.getApplicationSession();
        currentUser = backendController.getApplicationSession().getPrincipal();
      }
    }
    currentCallStack.add(new ActionEntry(action, new HashMap<>(context), startTimestamp));
  }

  /**
   * Action end.
   *
   * @param action
   *     the action
   * @param context
   *     the context
   */
  @Override
  public void actionEnd(IAction action, Map<String, Object> context) {
    if (!isEnabled() || !filter(action)) {
      return;
    }
    Date endTimestamp = new Date();
    int stackSize = currentCallStack.size();
    if (stackSize == 0) {
      LOG.warn("Action monitoring was called to dequeue the action stack but the action stack is empty");
      return;
    }
    ActionEntry actionEndEntry = currentCallStack.get(stackSize - 1);
    if (actionEndEntry.action != action) {
      LOG.warn(
          "Action monitoring was called to dequeue the action stack but the dequeued action is not the one that ended");
      return;
    }
    List<IAction> callStack = new ArrayList<>();
    for (ActionEntry previousActionEntry : currentCallStack) {
      callStack.add(previousActionEntry.action);
    }
    currentCallStack.remove(stackSize - 1);
    traceResults(currentSession, currentUser, currentWorkspace, currentModule, callStack, actionEndEntry.context,
        actionEndEntry.startTimestamp, endTimestamp);
    if (currentCallStack.isEmpty()) {
      currentSession = null;
      currentUser = null;
      currentWorkspace = null;
      currentModule = null;
    }
  }

  /**
   * Default filter : ommits FrontendAction, BackendAction and anonymous innerclass actions (FW).
   *
   * @param action
   *     the action
   * @return the boolean
   */
  protected boolean filter(IAction action) {
    Class<? extends IAction> actionClass = action.getClass();
    if (FrontendAction.class.equals(actionClass) || BackendAction.class.equals(actionClass) || actionClass
        .getSimpleName().isEmpty()) {
      return false;
    }
    return true;
  }

  /**
   * Is enabled boolean.
   *
   * @return the boolean
   */
  protected boolean isEnabled() {
    return true;
  }

  /**
   * Starts the plugin.
   *
   * @param context
   *     the context
   */
  @Override
  public synchronized void start(Map<String, Object> context) {
    if (!started) {
      IFrontendController<?, ?, ?> frontendController = getFrontendController(context);
      ActionsDirectoryBuilder actionsDirectoryBuilder = new ActionsDirectoryBuilder();
      actionsDirectoryBuilder.process(frontendController);
      traceUserActionsInventory(actionsDirectoryBuilder.getUserActions(), actionsDirectoryBuilder.getLovActions(),
          frontendController);
      started = true;
    }
  }
}
