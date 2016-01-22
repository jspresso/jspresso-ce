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
package org.jspresso.framework.application.backend;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A specialized transaction template that takes care of checking that the unit
 * of work is started when executing a transaction callback.
 *
 * @author Vincent Vandenschrick
 */
public class ControllerAwareTransactionTemplate extends TransactionTemplate {

  /**
   * Constructs a new {@code ControllerAwareTransactionTemplate} instance.
   */
  public ControllerAwareTransactionTemplate() {
    super();
  }

  /**
   * Constructs a new {@code ControllerAwareTransactionTemplate} instance.
   *
   * @param transactionManager the Spring transaction manager
   * @param transactionDefinition the Spring transaction definition
   */
  public ControllerAwareTransactionTemplate(
      PlatformTransactionManager transactionManager,
      TransactionDefinition transactionDefinition) {
    super(transactionManager, transactionDefinition);
  }

  /**
   * Constructs a new {@code ControllerAwareTransactionTemplate} instance.
   *
   * @param transactionManager the Spring transaction manager
   */
  public ControllerAwareTransactionTemplate(
      PlatformTransactionManager transactionManager) {
    super(transactionManager);
  }

  private static final long serialVersionUID = 7416468234402124464L;

  /**
   * Ensures that the controller has joined the transaction, starting a UoW if
   * necessary.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public <T> T execute(final TransactionCallback<T> action) {
    TransactionCallback<T> wrapper = new TransactionCallback<T>() {

      @Override
      public T doInTransaction(TransactionStatus status) {
        joinTransaction(status);
        return action.doInTransaction(status);
      }
    };
    return super.execute(wrapper);
  }

  /**
   * Join transaction.
   *
   * @param status
   *     the status
   */
  protected void joinTransaction(TransactionStatus status) {
    IBackendController backendController = BackendControllerHolder.getCurrentBackendController();
    // In case of 1st Tx or REQUIRES_NEW propagation behaviour, status.isNewTransaction() will be true,
    // thus instantiating a nested UOW is one is already started.
    backendController.joinTransaction(status.isNewTransaction());
  }

}
