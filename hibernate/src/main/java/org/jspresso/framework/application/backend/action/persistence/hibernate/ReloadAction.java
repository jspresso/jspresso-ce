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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.ObjectNotFoundException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * Reloads the entities provided by the context <code>ActionParameter</code>.
 * The whole entities graphs are reloaded from the persistent store.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ReloadAction extends AbstractHibernateAction {

  /**
   * Reloads the object(s) provided by the action context in a transaction.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    getController(context).clearPendingOperations();
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(TransactionStatus status) {
        HibernateTemplate hibernateTemplate = getHibernateTemplate(context);
        int oldFlushMode = hibernateTemplate.getFlushMode();
        try {
          // Temporary switch to a read-only session.
          hibernateTemplate.setFlushMode(HibernateAccessor.FLUSH_NEVER);

          List<IEntity> entitiesToReload = getEntitiesToReload(context);
          Exception deletedObjectEx = null;
          for (Iterator<IEntity> ite = entitiesToReload.iterator(); ite.hasNext();) {
            IEntity entity = ite.next();
            try {
              reloadEntity(entity, context);
            } catch (ObjectNotFoundException ex) {
              ite.remove();
              deletedObjectEx = ex;
            }
          }
          status.setRollbackOnly();
          if (deletedObjectEx != null) {
            throw new ConcurrencyFailureException(deletedObjectEx.getMessage(),
                deletedObjectEx);
          }
          return null;
        } finally {
          hibernateTemplate.setFlushMode(oldFlushMode);
        }
      }
    });
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the list of entities to reload.
   * 
   * @param context
   *          the action context.
   * @return the list of entities to save.
   */
  @SuppressWarnings("unchecked")
  protected List<IEntity> getEntitiesToReload(Map<String, Object> context) {
    return (List<IEntity>) getActionParameter(context);
  }
}
