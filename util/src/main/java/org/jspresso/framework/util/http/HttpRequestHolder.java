/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

/**
 * This filter needs to be installed on any broker servlet so that iot keeps
 * track of the current HTTP request.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HttpRequestHolder implements Filter {

  private static final ThreadLocal<HttpServletRequest> CURRENT_HTTP_REQUEST = new ThreadLocal<HttpServletRequest>();

  /**
   * Gets the current servlet request.
   * 
   * @return the current servlet request.
   */
  public static HttpServletRequest getServletRequest() {
    return CURRENT_HTTP_REQUEST.get();
  }

  /**
   * {@inheritDoc}
   */
  public void destroy() {
    // Nothing to clear.
  }

  /**
   * {@inheritDoc}
   */
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest) {
      CURRENT_HTTP_REQUEST.set((HttpServletRequest) request);
    }
    chain.doFilter(request, response);
    CURRENT_HTTP_REQUEST.set(null);
  }

  /**
   * {@inheritDoc}
   */
  public void init(@SuppressWarnings("unused") FilterConfig config) {
    // Nothing to init.
  }

}
