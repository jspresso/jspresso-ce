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

import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Saves the entities provided by the context {@code ActionParameter}. All
 * previously registered persistence operations are also performed.
 *
 * @author Vincent Vandenschrick
 */
public class SaveAction extends BackendAction {

  /**
   * Saves the object(s) provided by the action context in a transaction.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {

    List<IEntity> entitiesToSave = getEntitiesToSave(context);
    if (entitiesToSave != null) {
      IBackendController bc = getController(context);
      if (bc.isUnitOfWorkActive()) {
        entitiesToSave = bc.cloneInUnitOfWork(entitiesToSave);
      }
      for (IEntity entityToSave : entitiesToSave) {
        getController(context).registerForUpdate(entityToSave);
      }
    }

    getTransactionTemplate(context).execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            try {
              getController(context).performPendingOperations();
            } catch (RuntimeException ex) {
              getController(context).clearPendingOperations();
              throw ex;
            }
          }
        });
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the list of entities to save.
   *
   * @param context
   *          the action context.
   * @return the list of entities to save.
   */
  protected List<IEntity> getEntitiesToSave(Map<String, Object> context) {
    return getActionParameter(context);
  }
}
