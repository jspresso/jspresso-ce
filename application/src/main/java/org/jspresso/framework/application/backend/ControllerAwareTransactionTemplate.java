/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A specialized transaction template that takes care of checking that the unit
 * of work is started when executing a transaction callback.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerAwareTransactionTemplate extends TransactionTemplate {

  private static final long  serialVersionUID = 7416468234402124464L;

  private IBackendController controller;

  /**
   * Constructs a new <code>ControllerAwareTransactionTemplate</code> instance.
   * 
   * @param source
   *          the transaction template source.
   * @param controller
   *          the controller.
   */
  public ControllerAwareTransactionTemplate(TransactionTemplate source,
      IBackendController controller) {
    setIsolationLevel(source.getIsolationLevel());
    setName(source.getName());
    setPropagationBehavior(source.getPropagationBehavior());
    setReadOnly(source.isReadOnly());
    setTimeout(source.getTimeout());
    setTransactionManager(source.getTransactionManager());
    this.controller = controller;
  }

  /**
   * Ensures that the controller has joined the transaction, starting a UoW if
   * necessary.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Object execute(TransactionCallback action) {
    controller.joinTransaction();
    return super.execute(action);
  }

}
