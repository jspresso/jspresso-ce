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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionMonitoringPlugin;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.security.UserPrincipal;

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

  private IApplicationSession currentSession;
  private UserPrincipal       currentUser;
  private Workspace           currentWorkspace;
  private Module              currentModule;
  private List<ActionEntry>   currentCallStack;

  /**
   * Instantiates a new Abstract action monitoring plugin.
   */
  public AbstractActionMonitoringPlugin() {
    currentCallStack = new ArrayList<>();
  }

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
    if (!filter(action)) {
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
        currentSession = frontendController.getApplicationSession();
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
    if (!filter(action)) {
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
}
