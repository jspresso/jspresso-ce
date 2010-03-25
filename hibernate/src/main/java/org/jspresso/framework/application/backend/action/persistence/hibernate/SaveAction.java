/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Saves the entities provided by the context <code>ActionParameter</code>. All
 * previously registered persistence operations are also performed.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveAction extends AbstractHibernateAction {

  /**
   * Saves the object(s) provided by the action context in a transaction.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(
          @SuppressWarnings("unused") TransactionStatus status) {
        List<IEntity> entitiesToSave = getEntitiesToSave(context);
        if (entitiesToSave != null) {
          for (IEntity entityToSave : entitiesToSave) {
            getController(context).registerForUpdate(entityToSave);
          }
        }
        getController(context).performPendingOperations();
        return null;
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
  @SuppressWarnings("unchecked")
  protected List<IEntity> getEntitiesToSave(Map<String, Object> context) {
    return (List<IEntity>) getActionParameter(context);
  }
}
