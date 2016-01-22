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
package org.jspresso.framework.security;

import java.io.IOException;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that performs a JAAS login. The name of the JAAS configuration is
 * read from the servlet config. After successful login, username and password
 * are put in the session. Note that, in contrast to the
 * {@code SecurityContextFilter}, the login module used here is supposed to
 * perform a real authentication. With JBoss, you could use the
 * {@code UsersRolesLoginModule} for example. <br>
 * Note that this is sample implementation, and is written only in order to demo
 * the use of the {@link SecurityContextFilter} class. In a real life
 * implementation, one would probably never hard-code names of redirect urls,
 * names of form parameters and names of session attributes.
 */
public class LoginServlet extends HttpServlet {

  /**
   * {@code ERROR_DEFAULT}="login-error.html".
   */
  public static final String ERROR_DEFAULT        = "/login-error.html";

  /**
   * {@code ERROR_PARAM_NAME}"error.redirect".
   */
  public static final String ERROR_PARAM_NAME     = "error.redirect";
  /**
   * {@code JAAS_APPL_DEFAULT}="other".
   */
  public static final String JAAS_APPL_DEFAULT    = "other";

  /**
   * {@code JAAS_APPL_PARAM_NAME}="jaas-application".
   */
  public static final String JAAS_APPL_PARAM_NAME = "jaas-application";
  /**
   * {@code SUCCESS_DEFAULT}="main.html".
   */
  public static final String SUCCESS_DEFAULT      = "/main.html";

  /**
   * {@code SUCCESS_PARAM_NAME}="success.redirect".
   */
  public static final String SUCCESS_PARAM_NAME   = "success.redirect";
  private static final long  serialVersionUID     = 6326611145492998226L;

  private String             errorRedirectUrl;

  /**
   * The name of the JAAS application, the key for finding the JAAS module
   * configuration (e.g. in an auth.conf file). In JBoss this matches the
   * security domain name in the login-config.xml file.
   */
  private String             jaasApplicationName;
  private String             successRedirectUrl;

  /**
   * Processes the login form. The form is supposed to have two input values:
   * username and password.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (jaasApplicationName != null) {
      // retrieve form parameter values from request
      String username = request.getParameter("username");
      String password = request.getParameter("password");

      // just for demo purposes: do never login if no username is set
      if (username.trim().equals("")) {
        // redirect to the error page
        response.sendRedirect(request.getContextPath() + errorRedirectUrl);
        return;
      }

      // create a JAAS callback handler and hand it over username and password
      UsernamePasswordHandler callbackHandler = new UsernamePasswordHandler();
      callbackHandler.setUsername(username);
      callbackHandler.setPassword(password);

      // perform the JAAS login; it will callback on the callbackHandler to
      // obtain
      // username and password
      try {
        LoginContext loginContext = new LoginContext(jaasApplicationName,
            callbackHandler);
        loginContext.login();

        request.getSession().setAttribute("SUBJECT", loginContext.getSubject());

        // redirect to the main menu page
        response.sendRedirect(request.getContextPath() + successRedirectUrl);
      } catch (LoginException le) {
        // redirect to the error page
        response.sendRedirect(request.getContextPath() + errorRedirectUrl);
      }
    } else {
      request.getSession().setAttribute("SUBJECT",
          SecurityHelper.createAnonymousSubject());

      // redirect to the main menu page
      response.sendRedirect(request.getContextPath() + successRedirectUrl);
    }
  }

  /**
   * Initializes the servlet. Reads the name of the JAAS configuration.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void init(ServletConfig config) {
    jaasApplicationName = config.getInitParameter(JAAS_APPL_PARAM_NAME);
    if (jaasApplicationName == null) {
      jaasApplicationName = JAAS_APPL_DEFAULT;
    }

    successRedirectUrl = config.getInitParameter(SUCCESS_PARAM_NAME);
    if (successRedirectUrl == null) {
      successRedirectUrl = SUCCESS_DEFAULT;
    }

    errorRedirectUrl = config.getInitParameter(ERROR_PARAM_NAME);
    if (errorRedirectUrl == null) {
      errorRedirectUrl = ERROR_DEFAULT;
    }
  }
}
