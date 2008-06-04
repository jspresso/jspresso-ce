/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.persistence.hibernate;

import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.backend.session.hibernate.HibernateAwareApplicationSession;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Hibernate aware backend controller.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
