/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Collection;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.module.BeanModule;

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
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    getTransactionTemplate().execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        ICompositeValueConnector projectionConnector = getModuleConnector();
        BeanModule projection = (BeanModule) projectionConnector
            .getConnectorValue();
        if (projection.getModuleObjects() != null) {
          Collection projectedCollection = projection.getModuleObjects();
          for (Object entity : projectedCollection) {
            reloadEntity((IEntity) entity);
          }
        } else if (projection.getModuleObject() != null) {
          reloadEntity((IEntity) projection.getModuleObject());
        }
        return null;
      }
    });
    return null;
  }

  private void reloadEntity(IEntity entity) {
    if (entity.isPersistent()) {
      getApplicationSession().merge(
          (IEntity) getHibernateTemplate().load(entity.getContract().getName(),
              entity.getId()), MergeMode.MERGE_CLEAN_EAGER);
    }
  }
}
