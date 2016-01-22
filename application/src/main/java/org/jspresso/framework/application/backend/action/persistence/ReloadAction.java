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
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Reloads the entities provided by the context {@code ActionParameter}.
 * The whole entities graphs are reloaded from the persistent store.
 *
 * @author Vincent Vandenschrick
 */
public class ReloadAction extends BackendAction {

  private boolean transactional;

  /**
   * Instantiates a new Reload action.
   */
  public ReloadAction() {
    this.transactional = true;
  }

  /**
   * Reloads the object(s) provided by the action context in a transaction.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, final Map<String, Object> context) {
    getController(context).clearPendingOperations();
    final List<IEntity> entitiesToReload = getEntitiesToReload(context);
    if (isTransactional()) {
      getTransactionTemplate(context).execute(new TransactionCallbackWithoutResult() {
        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          iterateAndReload(entitiesToReload, context);
        }
      });
    } else {
      iterateAndReload(entitiesToReload, context);
    }
    return super.execute(actionHandler, context);
  }

  private void iterateAndReload(List<IEntity> entitiesToReload, Map<String, Object> context) {
    for (IEntity entity : entitiesToReload) {
      reloadEntity(entity, context);
    }
  }

  /**
   * Gets the list of entities to reload.
   *
   * @param context
   *          the action context.
   * @return the list of entities to save.
   */
  protected List<IEntity> getEntitiesToReload(Map<String, Object> context) {
    return getActionParameter(context);
  }

  /**
   * Is transactional.
   *
   * @return the boolean
   */
  public boolean isTransactional() {
    return transactional;
  }

  /**
   * Sets transactional.
   *
   * @param transactional the transactional
   */
  public void setTransactional(boolean transactional) {
    this.transactional = transactional;
  }
}
