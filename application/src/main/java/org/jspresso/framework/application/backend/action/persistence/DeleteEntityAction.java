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
package org.jspresso.framework.application.backend.action.persistence;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IComponent;

/**
 * An action used to delete the entity that is model of the view.
 * <p/>
 * Note that cleaning of relationships is a 2 pass process. The 1st one is a dry
 * run that checks that no functional exception is thrown by the business rules.
 * The second one performs the actual cleaning.
 *
 * @author Vincent Vandenschrick
 */
public class DeleteEntityAction extends BackendAction {

  /**
   * Retrieves the master and its managed collection from the model connector
   * then removes selected details from the managed collection.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    if (modelConnector == null) {
      return false;
    }
    try {
      IComponent componentToDelete = modelConnector.getConnectorValue();
      cleanRelationshipsOnDeletion(componentToDelete, context, true);
      cleanRelationshipsOnDeletion(componentToDelete, context, false);
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ActionException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ActionException(ex.getCause());
    }
    return super.execute(actionHandler, context);
  }
}
