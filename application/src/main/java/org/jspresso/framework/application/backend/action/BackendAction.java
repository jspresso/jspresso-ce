/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.action.AbstractAction;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This class should serve as base class for implementing a action which
 * executes on the backend (domain model) of the application. It provides
 * accessors on components which are generally used through the action execution
 * process.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 */
public class BackendAction extends AbstractAction {

  /**
   * This is a utility method which is able to retrieve the module model
   * connector this action has been executed on from its context. It uses
   * well-known context keys of the action context which is :
   * <ul>
   * <li> <code>ActionContextConstants.MODULE_MODEL_CONNECTOR</code>.
   * </ul>
   * 
   * @param context
   *          the action context.
   * @return the module model connector this action executes on.
   */
  protected ICompositeValueConnector getModuleConnector(
      Map<String, Object> context) {
    return (ICompositeValueConnector) ((ICompositeValueConnector) context
        .get(ActionContextConstants.MODULE_VIEW_CONNECTOR)).getModelConnector();
  }

  /**
   * {@inheritDoc}
   */
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
    return (IBackendController) context
        .get(ActionContextConstants.BACK_CONTROLLER);
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
}
