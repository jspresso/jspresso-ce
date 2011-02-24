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

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Abstract class for application startup including only the backend layer. This
 * serves for batch processes for instance.
 * 
 * @version $LastChangedRevision: 2097 $
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendStartup extends AbstractStartup {

  private IBackendController backendController;

  /**
   * Gets the application backend controller.
   * 
   * @return the application backend controller.
   */
  protected IBackendController getBackendController() {
    try {
      if (backendController == null) {
        backendController = (IBackendController) getApplicationContext()
            .getBean("applicationBackController");
      }
      return backendController;
    } catch (RuntimeException ex) {
      getLogger().error("applicationBackController could not be instanciated.",
          ex);
      throw ex;
    }
  }

  /**
   * Back controller is retrieved from the spring context and started.
   */
  protected void startController() {
    // start on brand new instances.
    backendController = null;
    getBackendController().start(getStartupLocale());
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
   * @param action the backend action to execute.
   * @param initialContext the initial action context.
   * @param subject the JAAS subject to execute the action for.
   * @param locale the locale used to execute the action.
   * @return true if the action execution was succesful.
   */
  protected boolean executeAction(BackendAction action,
      Map<String, Object> initialContext, Subject subject, Locale locale) {
    configureApplicationSession(subject, locale);
    Map<String, Object> startupActionContext = new HashMap<String, Object>();
    startupActionContext.putAll(getBackendController()
        .getInitialActionContext());
    startupActionContext.putAll(initialContext);
    boolean success = getBackendController().execute(action,
        startupActionContext);
    return success;
  }

}
