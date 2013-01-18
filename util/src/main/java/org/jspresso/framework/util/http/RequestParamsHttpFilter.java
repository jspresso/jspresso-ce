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
package org.jspresso.framework.util.http;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A simple servlet filter that takes the parameters out of the current HTTP
 * request and store them in a map into the HTTP session for later use by
 * subsequent requests.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RequestParamsHttpFilter implements Filter {

  /**
   * <code>REQUEST_PARAMS_KEY</code>.
   */
  public static final String REQUEST_PARAMS_KEY = "REQUEST_PARAMS_KEY";

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    Map<String, String> parameterMap = ((HttpServletRequest) request)
        .getParameterMap();
    if (parameterMap != null) {
      HttpSession session = ((HttpServletRequest) request).getSession();
      Map<String, String> existing = (Map<String, String>) session
          .getAttribute(REQUEST_PARAMS_KEY);
      if (existing == null) {
        existing = new LinkedHashMap<String, String>();
        session.setAttribute(REQUEST_PARAMS_KEY, existing);
      }
      existing.putAll(parameterMap);
    }
    chain.doFilter(request, response);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ServletException
   *           used in superclass.
   */

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // NO-OP
  }
}
