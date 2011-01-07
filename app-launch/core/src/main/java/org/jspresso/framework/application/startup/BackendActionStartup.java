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
package org.jspresso.framework.application.startup;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.security.UserPrincipal;

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
  private String              batchUserName;
  private Locale              startupLocale;

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
   */
  public void setBatchUserName(String batchUserName) {
    this.batchUserName = batchUserName;
  }

  /**
   * Sets the startupLocale.
   * 
   * @param startupLocale
   *          the startupLocale to set.
   */
  public void setStartupLocale(Locale startupLocale) {
    this.startupLocale = startupLocale;
  }

  /**
   * {@inheritDoc}
   */
  public void start() {
    startController();
    executeAction();
  }

  /**
   * Creates a default batch user subject.
   * 
   * @return a default batch user subject.
   */
  protected Subject createSubject() {
    Subject s = new Subject();
    UserPrincipal p = new UserPrincipal(getBatchUserName());
    s.getPrincipals().add(p);
    return s;
  }

  /**
   * Executes the backend action.
   * 
   * @return the action execution status.
   */
  protected boolean executeAction() {
    IApplicationSession batchSession = getBackendController()
        .getApplicationSession();
    batchSession.setLocale(getStartupLocale());
    batchSession.setSubject(createSubject());
    BackendAction backendAction = getAction();
    Map<String, Object> startupActionContext = new HashMap<String, Object>();
    startupActionContext.putAll(getBackendController()
        .getInitialActionContext());
    startupActionContext.putAll(getActionContext());
    boolean success = getBackendController().execute(backendAction,
        startupActionContext);
    return success;
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
   */
  protected String getBatchUserName() {
    return batchUserName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Locale getStartupLocale() {
    return startupLocale;
  }
}
