/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.persistence.hibernate;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.d2s.framework.application.backend.AbstractBackendController;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.hibernate.HibernateAwareApplicationSession;
import com.d2s.framework.model.entity.IEntityFactory;

/**
 * Hibernate aware backend controller.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateBackendController extends AbstractBackendController {

  private HibernateTemplate   hibernateTemplate;
  private TransactionTemplate transactionTemplate;

  /**
   * Gets the hibernateTemplate.
   * 
   * @return the hibernateTemplate.
   */
  public HibernateTemplate getHibernateTemplate() {
    return hibernateTemplate;
  }

  /**
   * Gets the transactionTemplate.
   * 
   * @return the transactionTemplate.
   */
  public TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setApplicationSession(IApplicationSession applicationSession) {
    if (!(applicationSession instanceof HibernateAwareApplicationSession)) {
      throw new IllegalArgumentException(
          "applicationSession must be an HibernateAwareApplicationSession.");
    }
    super.setApplicationSession(applicationSession);
    linkHibernateArtifacts();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEntityFactory(IEntityFactory entityFactory) {
    super.setEntityFactory(entityFactory);
    linkHibernateArtifacts();
  }

  /**
   * Sets the hibernateTemplate.
   * 
   * @param hibernateTemplate
   *            the hibernateTemplate to set.
   */
  public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
    this.hibernateTemplate = hibernateTemplate;
    linkHibernateArtifacts();
  }

  /**
   * Sets the transactionTemplate.
   * 
   * @param transactionTemplate
   *            the transactionTemplate to set.
   */
  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    this.transactionTemplate = transactionTemplate;
    linkHibernateArtifacts();
  }

  private void linkHibernateArtifacts() {
    if (getApplicationSession() != null && getHibernateTemplate() != null
        && getTransactionTemplate() != null && getEntityFactory() != null) {
      ApplicationSessionAwareEntityProxyInterceptor entityInterceptor = new ApplicationSessionAwareEntityProxyInterceptor();
      entityInterceptor.setApplicationSession(getApplicationSession());
      entityInterceptor.setEntityFactory(getEntityFactory());
      getHibernateTemplate().setEntityInterceptor(entityInterceptor);
      ((HibernateTransactionManager) getTransactionTemplate()
          .getTransactionManager()).setEntityInterceptor(entityInterceptor);
      ((HibernateAwareApplicationSession) getApplicationSession())
          .setHibernateTemplate(getHibernateTemplate());
    }
  }
}
