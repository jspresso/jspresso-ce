/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.d2s.framework.application.backend.action.AbstractBackendAction;
import com.d2s.framework.application.backend.persistence.hibernate.HibernateBackendController;
import com.d2s.framework.model.entity.IEntity;

/**
 * This the root abstract class of all hibernate related persistence actions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractHibernateAction extends AbstractBackendAction {

  /**
   * {@inheritDoc}
   */
  @Override
  protected HibernateBackendController getController(Map<String, Object> context) {
    return (HibernateBackendController) super.getController(context);
  }

  /**
   * Gets the hibernateTemplate.
   * 
   * @param context
   *          the action context.
   * @return the hibernateTemplate.
   */
  protected HibernateTemplate getHibernateTemplate(Map<String, Object> context) {
    return getController(context).getHibernateTemplate();
  }

  /**
   * Gets the transactionTemplate.
   * 
   * @param context
   *          the action context.
   * @return the transactionTemplate.
   */
  protected TransactionTemplate getTransactionTemplate(
      Map<String, Object> context) {
    return getController(context).getTransactionTemplate();
  }

  /**
   * This method must be called to (re)attach application session entities to
   * the current hibernate session.
   * 
   * @param entity
   *          the entity to merge.
   * @param hibernateSession
   *          the hibernate session
   * @param context
   *          the action context.
   * @return the merged entity.
   */
  protected IEntity mergeInHibernate(IEntity entity, Session hibernateSession,
      Map<String, Object> context) {
    return mergeInHibernate(Collections.singletonList(entity),
        hibernateSession, context).get(0);
  }

  /**
   * This method must be called to (re)attach application session entities to
   * the current hibernate session.
   * 
   * @param entities
   *          the entities to merge.
   * @param hibernateSession
   *          the hibernate session
   * @param context
   *          the action context.
   * @return the merged entity.
   */
  protected List<IEntity> mergeInHibernate(List<IEntity> entities,
      Session hibernateSession, Map<String, Object> context) {
    List<IEntity> mergedEntities = getApplicationSession(context)
        .cloneInUnitOfWork(entities);
    for (IEntity mergedEntity : mergedEntities) {
      if (mergedEntity.isPersistent()) {
        hibernateSession.lock(mergedEntity, LockMode.NONE);
      }
    }
    return mergedEntities;
  }
}
