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
package org.jspresso.framework.util.http;

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
 * This filter needs to be installed on any broker servlet so that iot keeps
 * track of the current HTTP request.
 *
 * @author Vincent Vandenschrick
 */
public class HttpRequestHolder implements Filter {

  private static final ThreadLocal<HttpServletRequest>  CURRENT_HTTP_REQUEST  = new InheritableThreadLocal<>();
  private static final ThreadLocal<HttpServletResponse> CURRENT_HTTP_RESPONSE = new InheritableThreadLocal<>();

  /**
   * Gets the current servlet request.
   *
   * @return the current servlet request.
   */
  public static final HttpServletRequest getServletRequest() {
    return CURRENT_HTTP_REQUEST.get();
  }

  /**
   * Assigns the servlet request for this current thread.
   *
   * @param request
   *          the servlet request.
   */
  public static final void setServletRequest(HttpServletRequest request) {
    CURRENT_HTTP_REQUEST.set(request);
  }

  /**
   * Gets the current servlet response.
   *
   * @return the current servlet response.
   */
  public static final HttpServletResponse getServletResponse() {
    return CURRENT_HTTP_RESPONSE.get();
  }

  /**
   * Assigns the servlet response for this current thread.
   *
   * @param response
   *          the servlet response.
   */
  public static final void setServletResponse(HttpServletResponse response) {
    CURRENT_HTTP_RESPONSE.set(response);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() {
    // Nothing to clear.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      setServletRequest((HttpServletRequest) request);
      setServletResponse((HttpServletResponse) response);
    }
    chain.doFilter(request, response);
    setServletRequest(null);
    setServletResponse(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(FilterConfig config) {
    // Nothing to init.
  }

  /**
   * Gets whether a thread-bound request is available.
   *
   * @return true if a thread-bound request is available.
   */
  public static boolean isAvailable() {
    return CURRENT_HTTP_REQUEST.get() != null;
  }

}
