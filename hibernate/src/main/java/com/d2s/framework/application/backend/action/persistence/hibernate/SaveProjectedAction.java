/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * Saves the projected object(s) in a transaction.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveProjectedAction extends AbstractHibernateAction {

  /**
   * Saves the projected object(s) in a transaction.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        ICompositeValueConnector moduleConnector = getModuleConnector(context);
        SubModule module = (SubModule) moduleConnector.getConnectorValue();
        if (module instanceof BeanCollectionModule) {
          for (Object entity : ((BeanCollectionModule) module)
              .getModuleObjects()) {
            saveEntity((IEntity) entity, context);
          }
        } else if (module instanceof BeanModule) {
          saveEntity((IEntity) ((BeanModule) module).getModuleObject(), context);
        }
        return null;
      }
    });
    return true;
  }

  /**
   * Saves an entity in hibernate.
   * 
   * @param entity
   *          the entity to save.
   */
  private void saveEntity(final IEntity entity,
      final Map<String, Object> context) {
    getHibernateTemplate(context).execute(new HibernateCallback() {

      public Object doInHibernate(Session session) {
        IEntity mergedEntity = mergeInHibernate(entity, session, context);
        session.saveOrUpdate(mergedEntity);
        return null;
      }
    });
  }
}
