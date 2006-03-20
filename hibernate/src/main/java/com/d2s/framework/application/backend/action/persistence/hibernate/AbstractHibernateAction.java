/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Collections;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.d2s.framework.application.backend.action.AbstractBackendAction;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.view.action.ActionContextConstants;

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

  private HibernateTemplate   hibernateTemplate;
  private TransactionTemplate transactionTemplate;

  /**
   * Gets the current application session.
   * 
   * @return the current application session.
   */
  protected IApplicationSession getApplicationSession() {
    return (IApplicationSession) getContext().get(
        ActionContextConstants.APPLICATION_SESSION);
  }

  /**
   * Gets the hibernateTemplate.
   * 
   * @return the hibernateTemplate.
   */
  protected HibernateTemplate getHibernateTemplate() {
    return hibernateTemplate;
  }

  /**
   * Sets the hibernateTemplate.
   * 
   * @param hibernateTemplate
   *          the hibernateTemplate to set.
   */
  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
  }

  /**
   * Gets the transactionTemplate.
   * 
   * @return the transactionTemplate.
   */
  protected TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  /**
   * This method must be called to (re)attach application session entities to
   * the current hibernate session.
   * 
   * @param entity
   *          the entity to merge.
   * @param hibernateSession
   *          the hibernate session
   * @return the merged entity.
   */
  protected IEntity mergeInHibernate(IEntity entity, Session hibernateSession) {
    return mergeInHibernate(Collections.singletonList(entity), hibernateSession)
        .get(0);
  }

  /**
   * This method must be called to (re)attach application session entities to
   * the current hibernate session.
   * 
   * @param entities
   *          the entities to merge.
   * @param hibernateSession
   *          the hibernate session
   * @return the merged entity.
   */
  protected List<IEntity> mergeInHibernate(List<IEntity> entities,
      Session hibernateSession) {
    List<IEntity> mergedEntities = getApplicationSession().cloneInUnitOfWork(
        entities);
    for (IEntity mergedEntity : mergedEntities) {
      if (mergedEntity.isPersistent()) {
        hibernateSession.lock(mergedEntity, LockMode.NONE);
        //hibernateSession.saveOrUpdate(mergedEntity);
      }
    }
    return mergedEntities;
  }

  /**
   * Sets the transactionTemplate.
   * 
   * @param transactionTemplate
   *          the transactionTemplate to set.
   */
  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    this.transactionTemplate = transactionTemplate;
  }
}
