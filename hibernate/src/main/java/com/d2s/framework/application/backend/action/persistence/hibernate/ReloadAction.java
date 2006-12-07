/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.model.entity.IEntity;

/**
 * Reloads the object(s) provided by the action context.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
    return true;
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
    return (List<IEntity>) context.get(ActionContextConstants.ACTION_PARAM);
  }
}
