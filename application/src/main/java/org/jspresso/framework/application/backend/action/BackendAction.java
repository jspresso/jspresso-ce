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
package org.jspresso.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.action.AbstractAction;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.async.AsyncActionExecutor;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.view.IView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * This class should serve as base class for implementing actions that execute
 * on the backend (domain model) of the application. It provides accessors on
 * the context elements that are generally used through the action execution
 * process.
 *
 * @author Vincent Vandenschrick
 */
public class BackendAction extends AbstractAction {

  private static final Logger LOG                      = LoggerFactory
                                                           .getLogger(BackendAction.class);

  private static final String WARN_BAD_ACCESS_DISABLED = "WARN_BAD_ACCESS_DISABLED";

  /**
   * {@code SELECTED_MODEL} is a static context key to inject into the test
   * context in order to bypass the query to the view to retrieve the selected
   * model.
   */
  public static final String  SELECTED_MODEL           = "SELECTED_MODEL";

  /**
   * {@code SELECTED_MODELS} is a static context key to inject into the
   * test context in order to bypass the query to the view to retrieve the
   * selected models.
   */
  public static final String  SELECTED_MODELS          = "SELECTED_MODELS";

  private boolean             badFrontendAccessChecked;

  /**
   * Constructs a new {@code BackendAction} instance.
   */
  public BackendAction() {
    setBadFrontendAccessChecked(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBackend() {
    return true;
  }

  /**
   * Gets the accessorFactory.
   *
   * @param context
   *          the action context.
   * @return the accessorFactory.
   */
  protected IAccessorFactory getAccessorFactory(Map<String, Object> context) {
    return getController(context).getAccessorFactory();
  }

  /**
   * Gets the current application session.
   *
   * @param context
   *          the action context.
   * @return the current application session.
   */
  protected IApplicationSession getApplicationSession(
      Map<String, Object> context) {
    return getController(context).getApplicationSession();
  }

  /**
   * Performs necessary cleanings when an entity or component is deleted.
   *
   * @param component
   *          the deleted entity or component.
   * @param context
   *          The action context.
   * @param dryRun
   *          set to true to simulate before actually doing it.
   * @throws IllegalAccessException
   *           whenever this kind of exception occurs.
   * @throws java.lang.reflect.InvocationTargetException
   *           whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *           whenever this kind of exception occurs.
   */
  protected void cleanRelationshipsOnDeletion(IComponent component,
      Map<String, Object> context, boolean dryRun)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    getController(context).cleanRelationshipsOnDeletion(component, dryRun);
  }

  /**
   * Gets the frontend controller out of the action context.
   *
   * @param context
   *          the action context.
   * @return the frontend controller.
   */
  @Override
  protected IBackendController getController(Map<String, Object> context) {
    return getBackendController(context);
  }

  /**
   * Gets the entityFactory.
   *
   * @param context
   *          the action context.
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory(Map<String, Object> context) {
    return getController(context).getEntityFactory();
  }

  /**
   * Gets the transactionTemplate.
   *
   * @param context
   *          the action context.
   * @return the transactionTemplate.
   */
  protected TransactionTemplate getTransactionTemplate(
      Map<String, Object> context) {
    return getController(context).getTransactionTemplate();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected <T> T getSelectedModel(int[] viewPath, Map<String, Object> context) {
    boolean wbad = context.containsKey(WARN_BAD_ACCESS_DISABLED);
    try {
      if (viewPath == null) {
        // we don't warn about anything if we only query the selected model
        // since it's supported now by injecting a SELECTED_MODEL variable in
        // the context during testing.
        if (context.containsKey(SELECTED_MODEL)) {
          return (T) context.get(SELECTED_MODEL);
        }
        context.put(WARN_BAD_ACCESS_DISABLED, null);
      }
      return super.getSelectedModel(viewPath, context);
    } finally {
      if (!wbad) {
        context.remove(WARN_BAD_ACCESS_DISABLED);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected <T> List<T> getSelectedModels(int[] viewPath,
      Map<String, Object> context) {
    boolean wbad = context.containsKey(WARN_BAD_ACCESS_DISABLED);
    try {
      if (viewPath == null) {
        // we don't warn about anything if we only query the selected model
        // since it's supported now by injecting a SELECTED_MODELS variable in
        // the context during testing.
        if (context.containsKey(SELECTED_MODELS)) {
          return (List<T>) context.get(SELECTED_MODELS);
        }
        context.put(WARN_BAD_ACCESS_DISABLED, null);
      }
      return super.getSelectedModels(viewPath, context);
    } finally {
      if (!wbad) {
        context.remove(WARN_BAD_ACCESS_DISABLED);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected <T> IView<T> getView(int[] viewPath, Map<String, Object> context) {
    warnBadFrontendAccess(context);
    return super.getView(viewPath, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IValueConnector getViewConnector(int[] viewPath,
      Map<String, Object> context) {
    warnBadFrontendAccess(context);
    return super.getViewConnector(viewPath, context);
  }

  /**
   * Allows to disable frontend access checks from backend actions. This is
   * sometimes necessary to avoid over-complicated refactoring when the access
   * is accepted by the dev team.
   *
   * @return {@code true} by default, i.e. bad frontend access detection is
   *         enabled.
   */
  protected boolean isBadFrontendAccessChecked() {
    return badFrontendAccessChecked;
  }

  /**
   * Sets the badFrontendAccessChecked.
   *
   * @param badFrontendAccessChecked
   *          the badFrontendAccessChecked to set.
   */
  public void setBadFrontendAccessChecked(boolean badFrontendAccessChecked) {
    this.badFrontendAccessChecked = badFrontendAccessChecked;
  }

  private void warnBadFrontendAccess(Map<String, Object> context) {
    if (isBadFrontendAccessChecked()
        && !context.containsKey(WARN_BAD_ACCESS_DISABLED)) {
      LOG.warn(
          "Access to frontend context detected from a backend action which is strongly discouraged. "
              + "{} should use either the action parameter or a specific variable.",
          getClass().getName());
    }
  }

  /**
   * Inform about the action progress.
   *
   * @param progress
   *          the action progress.
   */
  protected void setProgress(double progress) {
    if (Thread.currentThread() instanceof AsyncActionExecutor) {
      ((AsyncActionExecutor) Thread.currentThread()).setProgress(progress);
    }
  }

  /**
   * Reloads an entity in mongo.
   *
   * @param entity
   *          the entity to reload.
   * @param context
   *          the action context.
   */
  protected void reloadEntity(IEntity entity, Map<String, Object> context) {
    getController(context).reload(entity);
  }
}
