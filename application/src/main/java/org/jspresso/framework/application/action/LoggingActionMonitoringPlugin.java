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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.lov.LovAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A simple action monitoring plugin that logs the actions executions.
 *
 * @author Vincent Vandenschrick
 */
public class LoggingActionMonitoringPlugin extends AbstractActionMonitoringPlugin {

  private static final Logger LOG = LoggerFactory.getLogger(LoggingActionMonitoringPlugin.class);

  @Override
  protected void traceResults(IApplicationSession session, UserPrincipal user, Workspace workspace, Module module,
                              List<IAction> callStack, Map<String, Object> context, Date startTimestamp,
                              Date endTimestamp) {
    if (isEnabled()) {
      String workspaceName = "";
      if (workspace != null) {
        workspaceName = workspace.getName();
      }
      String moduleName = "";
      if (module != null) {
        moduleName = module.getName();
        if (moduleName == null) {
          moduleName = module.getI18nName();
        }
      }
      String sessionId = "";
      if (session != null) {
        sessionId = session.getId();
      }
      String userName = "";
      if (user != null) {
        userName = user.getName();
      }
      String actionType = null;
      StringBuilder callStackNames = new StringBuilder();
      for (IAction action : callStack) {
        String actionDescription = getActionDescription(action, context);
        callStackNames.append(actionDescription).append("|");
        actionType = action.isBackend() ? "B" : "F";
      }
      callStackNames.delete(callStackNames.length() - 1, callStackNames.length());
      SimpleDateFormat tsFormat = new SimpleDateFormat("HH:mm:ss.SSS");
      String startTs = tsFormat.format(startTimestamp);
      String endTs = tsFormat.format(endTimestamp);
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      String date = dateFormat.format(startTimestamp);
      long duration = endTimestamp.getTime() - startTimestamp.getTime();
      LOG.trace("[{}][{}][{}][{}][{}][{}][{}][{}][{}][{} ms.]", sessionId, userName, actionType, date, startTs, endTs,
          workspaceName, moduleName, callStackNames, duration);
    }
  }

  /**
   * Trace user actions inventory.
   *
   * @param userActions
   *     the user actions
   * @param frontendController
   *     the frontend controller
   */
  @Override
  protected void traceUserActionsInventory(Set<IDisplayableAction> userActions,
                                           Map<String, IDisplayableAction> lovActions,
                                           IFrontendController<?, ?, ?> frontendController) {
    if (isEnabled()) {
      StringBuilder userActionsInventory = new StringBuilder();
      for (IDisplayableAction userAction : userActions) {
        String actionDescription = getActionDescription(userAction, Collections.<String, Object>emptyMap());
        String actionNameEN = userAction.getI18nName(frontendController, Locale.ENGLISH);
        String actionNameFR = userAction.getI18nName(frontendController, Locale.FRENCH);
        appendToInventory(userActionsInventory, actionDescription, actionNameEN, actionNameFR);
      }
      for (Map.Entry<String, IDisplayableAction> lovActionEntry : lovActions.entrySet()) {
        String referencePropertyName = lovActionEntry.getKey();
        IDisplayableAction lovAction = lovActionEntry.getValue();

        String actionDescription = getActionDescription(lovAction, Collections.<String, Object>emptyMap()) + "("
            + referencePropertyName + ")";
        String actionNameEN = "L.O.V. " + frontendController.getTranslation(referencePropertyName, Locale.ENGLISH);
        String actionNameFR = "L.O.V. " + frontendController.getTranslation(referencePropertyName, Locale.FRENCH);
        appendToInventory(userActionsInventory, actionDescription, actionNameEN, actionNameFR);
      }
      LOG.trace("###### Start User actions inventory ######");
      LOG.trace(userActionsInventory.toString());
      LOG.trace("######  End User actions inventory  ######");
    }
  }

  private void appendToInventory(StringBuilder userActionsInventory, String actionDescription, String actionNameEN,
                                 String actionNameFR) {
    userActionsInventory.append(
        "[" + actionDescription + "][" + Locale.ENGLISH + ":" + actionNameEN + "|" + Locale.FRENCH + ":"
            + actionNameFR + "]\n");
  }

  private String getActionDescription(IAction action, Map<String, Object> context) {
    Class<? extends IAction> actionClass = action.getClass();
    String actionDescription = actionClass.getSimpleName();
    if (actionDescription == null || actionDescription.isEmpty()) {
      actionDescription = actionClass.getName();
      int lastDotIndex = actionDescription.lastIndexOf(".");
      if (lastDotIndex >= 0) {
        actionDescription = actionDescription.substring(lastDotIndex + 1);
      }
    }
    if (action instanceof LovAction<?, ?, ?>) {
      IReferencePropertyDescriptor<IComponent> componentRefDescriptor = (IReferencePropertyDescriptor<IComponent>)
          context
          .get(CreateQueryComponentAction.COMPONENT_REF_DESCRIPTOR);
      if (componentRefDescriptor != null) {
        String referencePropertyName = componentRefDescriptor.getName();
        if (referencePropertyName != null) {
          int lastDotIndex = referencePropertyName.lastIndexOf(".");
          if (lastDotIndex >= 0) {
            referencePropertyName = referencePropertyName.substring(lastDotIndex + 1);
          }
        }
        actionDescription = actionDescription + "(" + referencePropertyName + ")";
      }
    }
    return actionDescription;
  }

  @Override
  protected boolean isEnabled() {
    return LOG.isTraceEnabled();
  }
}
