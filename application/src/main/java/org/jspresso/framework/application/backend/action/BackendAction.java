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
package org.jspresso.framework.application.backend.action;

import java.util.Map;

import org.jspresso.framework.application.action.AbstractAction;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.binding.IValueConnector;
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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BackendAction extends AbstractAction {

  private static final Logger LOG = LoggerFactory
                                      .getLogger(BackendAction.class);

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
  @Override
  protected Object getSelectedModel(int[] viewPath, Map<String, Object> context) {
    warnBadFrontendAccess();
    return super.getSelectedModel(viewPath, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IView<?> getView(int[] viewPath, Map<String, Object> context) {
    warnBadFrontendAccess();
    return super.getView(viewPath, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IValueConnector getViewConnector(int[] viewPath,
      Map<String, Object> context) {
    warnBadFrontendAccess();
    return super.getViewConnector(viewPath, context);
  }

  private void warnBadFrontendAccess() {
    LOG.warn(
        "Access to frontend context detected from a backend action which is strongly discouraged. "
            + "{} should use either the action parameter or a specific variable.",
        getClass().getName());
  }
}
