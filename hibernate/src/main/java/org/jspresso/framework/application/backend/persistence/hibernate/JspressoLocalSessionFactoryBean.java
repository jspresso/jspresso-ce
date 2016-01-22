/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Used to register Jspresso global filter.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoLocalSessionFactoryBean extends LocalSessionFactoryBean {

  /**
   * Registers the default Jspresso Filter.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sfb) {
    Map<String, Type> filterParameters = new HashMap<>();
    filterParameters.put(
        HibernateBackendController.JSPRESSO_SESSION_GLOBALS_LOGIN, sfb
            .getTypeResolver().heuristicType("string"));
    filterParameters.put(
        HibernateBackendController.JSPRESSO_SESSION_GLOBALS_LANGUAGE, sfb
            .getTypeResolver().heuristicType("string"));
    sfb.addFilterDefinition(new FilterDefinition(
        HibernateBackendController.JSPRESSO_SESSION_GLOBALS, null,
        filterParameters));
    return super.buildSessionFactory(sfb);
  }

  /**
   * Receives a transaction manager. It will apply it if and only if it is a Spring supported JTA TX manager.
   * @param transactionManager the transaction manager.
   */
  @Override
  public void setJtaTransactionManager(Object transactionManager) {
    if (transactionManager instanceof JtaTransactionManager
        || transactionManager instanceof TransactionManager) {
      super.setJtaTransactionManager(transactionManager);
    }
    // Do nothing if it's not supported.
  }
}
