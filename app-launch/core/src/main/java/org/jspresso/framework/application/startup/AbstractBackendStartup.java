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
package org.jspresso.framework.application.startup;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.security.auth.Subject;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for application startup including only the backend layer. This
 * serves for batch processes for instance.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendStartup extends AbstractStartup {

  private Locale              startupLocale;
  private TimeZone            clientTimeZone;

  private static final Logger LOG = LoggerFactory
                                      .getLogger(AbstractBackendStartup.class);

  /**
   * Gets the application backend controller.
   *
   * @return the application backend controller.
   */
  protected IBackendController getBackendController() {
    return BackendControllerHolder.getCurrentBackendController();
  }

  /**
   * Back controller is retrieved from the spring context and started.
   */
  protected void startController() {
    try {
      // start on brand new instance.
      IBackendController backendController = (IBackendController) getApplicationContext()
          .getBean("applicationBackController");
      BackendControllerHolder.setThreadBackendController(backendController);
      backendController.start(getStartupLocale(), getClientTimeZone());
    } catch (RuntimeException ex) {
      LOG.error("applicationBackController could not be instantiated.", ex);
      throw ex;
    }
  }

  /**
   * Creates a user subject.
   *
   * @param userName
   *          the user name.
   * @return a user subject.
   */
  protected Subject createSubject(String userName) {
    Subject s = new Subject();
    UserPrincipal p = new UserPrincipal(userName);
    s.getPrincipals().add(p);
    return s;
  }

  /**
   * Configures session on the backend controller.
   *
   * @param subject
   *          the JAAS subject containing the user principal.
   * @param locale
   *          the locale to use.
   */
  protected void configureApplicationSession(Subject subject, Locale locale) {
    IApplicationSession session = getBackendController()
        .getApplicationSession();
    session.setSubject(subject);
    session.setLocale(locale);
  }

  /**
   * Executes a backend action.
   *
   * @param action
   *          the backend action to execute.
   * @param initialContext
   *          the initial action context.
   * @param subject
   *          the JAAS subject to execute the action for.
   * @param locale
   *          the locale used to execute the action.
   * @return true if the action execution was successful.
   */
  protected boolean executeAction(BackendAction action,
      Map<String, Object> initialContext, Subject subject, Locale locale) {
    configureApplicationSession(subject, locale);
    Map<String, Object> startupActionContext = new HashMap<>();
    startupActionContext.putAll(getBackendController()
        .getInitialActionContext());
    startupActionContext.putAll(initialContext);
    boolean success = getBackendController().execute(action,
        startupActionContext);
    return success;
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
  @Override
  protected Locale getStartupLocale() {
    if (startupLocale == null) {
      return Locale.getDefault();
    }
    return startupLocale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    startController();
  }

  /**
   * Gets the clientTimeZone.
   *
   * @return the clientTimeZone.
   */
  @Override
  public TimeZone getClientTimeZone() {
    if (clientTimeZone == null) {
      return TimeZone.getDefault();
    }
    return clientTimeZone;
  }

  /**
   * Sets the clientTimeZone.
   *
   * @param clientTimeZone
   *          the clientTimeZone to set.
   */
  public void setClientTimeZone(TimeZone clientTimeZone) {
    this.clientTimeZone = clientTimeZone;
  }
}
