/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;


/**
 * Reloads the object(s) provided by the action context.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    getApplicationSession(context).clearPendingOperations();
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        List<IEntity> objectsToSave = getEntitiesToReload(context);
        for (IEntity entity : objectsToSave) {
          reloadEntity(entity, context);
        }
        status.setRollbackOnly();
        return null;
      }
    });
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the list of entities to reload.
   * 
   * @param context
   *            the action context.
   * @return the list of entities to save.
   */
  @SuppressWarnings("unchecked")
  protected List<IEntity> getEntitiesToReload(Map<String, Object> context) {
    return (List<IEntity>) context.get(ActionContextConstants.ACTION_PARAM);
  }
}
