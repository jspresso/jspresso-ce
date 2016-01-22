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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple filter to ensure that the session is authenticated and contains a
 * Subject.
 *
 * @author Vincent Vandenschrick
 */
public class SecurityContextFilter implements Filter {

  /**
   * {@code LOGIN_DEFAULT}="login.html".
   */
  public static final String LOGIN_DEFAULT    = "/login.html";
  /**
   * {@code LOGIN_PARAM_NAME}="login.redirect".
   */
  public static final String LOGIN_PARAM_NAME = "login.redirect";

  private String             loginRedirectUrl;

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() {
    // NO-OP.
  }

  /**
   * If the session is not authenticated, redirects to the login page.
   * {@inheritDoc}
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    if (((HttpServletRequest) request).getSession().getAttribute("SUBJECT") != null) {
      chain.doFilter(request, response);
    } else {
      ((HttpServletResponse) response)
          .sendRedirect(((HttpServletRequest) request).getContextPath()
              + loginRedirectUrl);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(FilterConfig config) {
    loginRedirectUrl = config.getInitParameter(LOGIN_PARAM_NAME);
    if (loginRedirectUrl == null) {
      loginRedirectUrl = LOGIN_DEFAULT;
    }
  }
}
