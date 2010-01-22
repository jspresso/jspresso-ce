/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.startup.batch;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.startup.AbstractBackendStartup;

/**
 * A simple batch process starter. The batch itself is coded as a backend action
 * that is executed by the backend controller.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BatchActionStartup extends AbstractBackendStartup {

  private String              applicationContextKey;
  private Locale              startupLocale;
  private Map<String, Object> actionContext;
  private String              actionBeanId;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return applicationContextKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Locale getStartupLocale() {
    return startupLocale;
  }

  /**
   * Gets the initial parameterized action context.
   * 
   * @return the initial parameterized action context.
   */
  protected Map<String, Object> getActionContext() {
    return actionContext;
  }

  /**
   * The bean id of the backend action to execute.
   * 
   * @return bean id of the backend action to execute.
   */
  protected String getActionBeanId() {
    return actionBeanId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    super.start();
    BackendAction backendAction = (BackendAction) getApplicationContext()
        .getBean(getActionBeanId());
    Map<String, Object> startupActionContext = new HashMap<String, Object>();
    startupActionContext.putAll(getBackendController()
        .getInitialActionContext());
    startupActionContext.putAll(getActionContext());
    boolean success = getBackendController().execute(backendAction,
        startupActionContext);
    if (success) {
      System.exit(0);
    }
    System.exit(1);
  }
}
