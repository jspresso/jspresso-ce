/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.startup;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.BackendAction;

/**
 * A backend startup implementation whose implementation is a backend action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BackendActionStartup extends AbstractBackendStartup {

  private String              actionBeanId;
  private Map<String, Object> actionContext;
  private String              applicationContextKey;
  private String              userName;

  /**
   * Sets the actionBeanId.
   * 
   * @param actionBeanId
   *          the actionBeanId to set.
   */
  public void setActionBeanId(String actionBeanId) {
    this.actionBeanId = actionBeanId;
  }

  /**
   * Sets the actionContext.
   * 
   * @param actionContext
   *          the actionContext to set.
   */
  public void setActionContext(Map<String, Object> actionContext) {
    this.actionContext = actionContext;
  }

  /**
   * Sets the applicationContextKey.
   * 
   * @param applicationContextKey
   *          the applicationContextKey to set.
   */
  public void setApplicationContextKey(String applicationContextKey) {
    this.applicationContextKey = applicationContextKey;
  }

  /**
   * Sets the batchUserName.
   * 
   * @param batchUserName
   *          the batchUserName to set.
   * @deprecated use setUserName instead.
   */
  @Deprecated
  public void setBatchUserName(String batchUserName) {
    setUserName(batchUserName);
  }

  /**
   * Sets the userName.
   * 
   * @param userName
   *          the userName to set.
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    try {
      super.start();
      executeAction();
    } finally {
      IBackendController bc = BackendControllerHolder.getCurrentBackendController();
      if (bc != null) {
        bc.cleanupRequestResources();
      }
    }
  }

  /**
   * Creates a user subject.
   * 
   * @return a user subject.
   */
  protected Subject createSubject() {
    return createSubject(getUserName());
  }

  /**
   * Executes the backend action.
   * 
   * @return the action execution status.
   */
  protected boolean executeAction() {
    return executeAction(getAction(), getActionContext(), createSubject(getUserName()), getStartupLocale());
  }

  /**
   * Retrieves the backend action to execute.
   * 
   * @return the backend action to execute.
   */
  protected BackendAction getAction() {
    return (BackendAction) getApplicationContext().getBean(getActionBeanId());
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
   * Gets the initial parameterized action context.
   * 
   * @return the initial parameterized action context.
   */
  protected Map<String, Object> getActionContext() {
    if (actionContext == null) {
      actionContext = new HashMap<String, Object>();
    }
    return actionContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return applicationContextKey;
  }

  /**
   * Gets the batchUserName.
   * 
   * @return the batchUserName.
   * @deprecated use getUserName instead
   */
  @Deprecated
  protected String getBatchUserName() {
    return userName;
  }

  /**
   * Gets the userName.
   * 
   * @return the userName.
   */
  protected String getUserName() {
    return userName;
  }
}
