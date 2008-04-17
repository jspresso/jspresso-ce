/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.model.entity.IEntity;

/**
 * Saves the object(s) provided by the action context.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    getTransactionTemplate(context).execute(new TransactionCallback() {

      @SuppressWarnings("unchecked")
      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        List<IEntity> entitiesToSave = getEntitiesToSave(context);
        for (IEntity entity : entitiesToSave) {
          saveEntity(entity, context);
        }
        return null;
      }
    });
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the list of entities to save.
   * 
   * @param context
   *            the action context.
   * @return the list of entities to save.
   */
  @SuppressWarnings("unchecked")
  protected List<IEntity> getEntitiesToSave(Map<String, Object> context) {
    return (List<IEntity>) context.get(ActionContextConstants.ACTION_PARAM);
  }
}
