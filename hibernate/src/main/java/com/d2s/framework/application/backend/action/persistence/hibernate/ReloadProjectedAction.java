/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Collection;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * Reloads the projected object(s) in a transaction.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ReloadProjectedAction extends AbstractHibernateAction {

  /**
   * Reloads the projected object(s) in a transaction.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    getApplicationSession(context).clearPendingOperations();
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        ICompositeValueConnector moduleConnector = getModuleConnector(context);
        SubModule module = (SubModule) moduleConnector.getConnectorValue();
        if (module instanceof BeanCollectionModule) {
          Collection projectedCollection = ((BeanCollectionModule) module)
              .getModuleObjects();
          for (Object entity : projectedCollection) {
            reloadEntity((IEntity) entity, context);
          }
        } else if (module instanceof BeanModule) {
          reloadEntity((IEntity) ((BeanModule) module).getModuleObject(),
              context);
        }
        status.setRollbackOnly();
        return null;
      }
    });
    return true;
  }

  private void reloadEntity(IEntity entity, Map<String, Object> context) {
    if (entity.isPersistent()) {
      HibernateTemplate hibernateTemplate = getHibernateTemplate(context);
      getApplicationSession(context).merge(
          (IEntity) hibernateTemplate.load(entity.getContract().getName(),
              entity.getId()), MergeMode.MERGE_CLEAN_EAGER);
    }
  }
}
