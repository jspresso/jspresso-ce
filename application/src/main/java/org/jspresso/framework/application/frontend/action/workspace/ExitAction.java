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
package org.jspresso.framework.application.frontend.action.workspace;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;

import java.util.List;
import java.util.Map;

/**
 * This action exits the application.
 * Before doing so, user activated application modules are traversed
 * to check that no pending changes need to be forwarded to the persistent
 * store.
 * Also asynchronous executor thread are traversed to check for still alive
 * one.
 * Whenever the dirty checking is positive, or any asynchronous executor is
 * still alive, then the user is notified and given a chance to cancel the exit.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ExitAction<E, F, G> extends FrontendAction<E, F, G> {

  private final IAction actualExitAction;
  private IAction checkCurrentModuleDirtyStateAction;

  /**
   * Constructs a new {@code ExitAction} instance.
   */
  public ExitAction() {
    actualExitAction = new FrontendAction<E, F, G>() {

      @Override
      public boolean execute(IActionHandler actionHandler,
          Map<String, Object> context) {
        return getController(context).stop();
      }
    };
  }

  /**
   * Stops the frontend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    String checkBeforeExitMsg = checkBeforeExitFrontAction(actionHandler, context);
    if (checkBeforeExitMsg!=null) {

      checkBeforeExitMsg = "<html>" + checkBeforeExitMsg + "<br><br>"
              + getTranslationProvider(context).getTranslation("exit.question.message", getLocale(context))
              + "</html>";

      getController(context).popupYesNo(
              getSourceComponent(context),
              getTranslationProvider(context).getTranslation("module.content.dirty.title", getLocale(context)),
              getIconFactory(context).getQuestionIconImageURL(),
              checkBeforeExitMsg,
              actualExitAction, null, context);
    }
    else {

      return actualExitAction.execute(actionHandler, context);
    }

    return false;
  }

  /**
   * Check before exiting.
   * 1/ Traverse module to check that no pending changes need to be forwarded
   * 2/ Traverse asynchronous executor to check that none if them are still alive
   * @param context
   *             The context.
   * @return the i18n message to display or null
   */
  protected String checkBeforeExitFrontAction(IActionHandler actionHandler, Map<String, Object> context) {

    StringBuilder sb = new StringBuilder();

    if (hasDirtyModules(actionHandler, context)) {
      sb.append(getTranslationProvider(context).getTranslation(
                            "module.content.dirty.message", getLocale(context)));
    }

    if (hasAliveAsynchronousExecutors(actionHandler, context)) {
      if (sb.length()>0) {
        sb.append("<br/>");
      }
      sb.append(getTranslationProvider(context).getTranslation(
              "asynchronous.tasks.alive.message", getLocale(context)));
    }

    return sb.length()==0 ? null : sb.toString();
  }

  /**
   * Traverse module to check that no pending changes need to be forwarded
   * @param actionHandler
   *          the action handler this action has been told to execute by. It may
   *          be used to post another action execution upon completion of this
   *          one.
   * @param context
   *          the execution context. The action should update it depending on
   *          its result.
   * @return true if one or more module is dirty.
   */
  protected boolean hasDirtyModules(IActionHandler actionHandler, Map<String, Object> context) {

    if (getCheckCurrentModuleDirtyStateAction() != null) {
      getCheckCurrentModuleDirtyStateAction().execute(actionHandler, context);
    }

    boolean hasDirtyModules = false;
    for (String workspaceName : getController(context).getWorkspaceNames()) {
      Workspace ws = getController(context).getWorkspace(workspaceName);
      List<Module> modules = ws.getModules();
      if (modules != null) {
        for (Module m : modules) {
          if (isDirtyInDepth(m)) {
            hasDirtyModules = true;
            break;
          }
        }
      }
      if (hasDirtyModules) {
        break;
      }
    }

    return hasDirtyModules;
  }

  /**
   * Traverse asynchronous executor to check that none are still alive
   * @param actionHandler
   *          the action handler this action has been told to execute by. It may
   *          be used to post another action execution upon completion of this
   *          one.
   * @param context
   *          the execution context. The action should update it depending on
   *          its result.
   * @return true if one or more module is dirty.
   */
  protected boolean hasAliveAsynchronousExecutors(IActionHandler actionHandler, Map<String, Object> context) {

    return !getBackendController(context).getRunningExecutors().isEmpty();
  }


  /**
   * Configures the action used to perform dirty checking on current module to
   * update its dirty state. It will be triggered just before a global iteration
   * is performed on all the application modules to be able to notify the user
   * that pending changes are not yet flushed to the persistent store.
   *
   * @param checkCurrentModuleDirtyStateAction
   *          the checkCurrentModuleDirtyStateAction to set.
   */
  public void setCheckCurrentModuleDirtyStateAction(
      IAction checkCurrentModuleDirtyStateAction) {
    this.checkCurrentModuleDirtyStateAction = checkCurrentModuleDirtyStateAction;
  }

  /**
   * Gets the checkCurrentModuleDirtyStateAction.
   *
   * @return the checkCurrentModuleDirtyStateAction.
   */
  protected IAction getCheckCurrentModuleDirtyStateAction() {
    return checkCurrentModuleDirtyStateAction;
  }

  private boolean isDirtyInDepth(Module module) {
    if (module.isDirty()) {
      return true;
    }
    List<Module> subModules = module.getSubModules();
    if (subModules != null) {
      for (Module subModule : subModules) {
        if (isDirtyInDepth(subModule)) {
          return true;
        }
      }
    }
    return false;
  }

}
