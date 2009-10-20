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
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SecurityContextFilter implements Filter {

  /**
   * <code>LOGIN_DEFAULT</code>="login.html".
   */
  public static final String LOGIN_DEFAULT    = "/login.html";
  /**
   * <code>LOGIN_PARAM_NAME</code>="login.redirect".
   */
  public static final String LOGIN_PARAM_NAME = "login.redirect";

  private String             loginRedirectUrl;

  /**
   * {@inheritDoc}
   */
  public void destroy() {
    // NO-OP.
  }

  /**
   * If the session is not authenticated, redirects to the login page.
   * {@inheritDoc}
   */
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
  public void init(FilterConfig config) {
    loginRedirectUrl = config.getInitParameter(LOGIN_PARAM_NAME);
    if (loginRedirectUrl == null) {
      loginRedirectUrl = LOGIN_DEFAULT;
    }
  }
}
