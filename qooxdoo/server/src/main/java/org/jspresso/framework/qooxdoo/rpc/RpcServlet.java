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
package org.jspresso.framework.qooxdoo.rpc;

import javax.servlet.http.HttpServletRequest;

import net.sf.qooxdoo.rpc.RemoteCallUtils;

/**
 * Subclass of Qooxdoo rpc servlet to handle JSON <-> Java more finely.
 *
 * @author Vincent Vandenschrick
 */
public class RpcServlet extends net.sf.qooxdoo.rpc.RpcServlet {

  private static final long serialVersionUID = -3112390742440209121L;

  /**
   * Fixes an ArrayIndesOutOfBounds on the application when it is deployed in the root context.
   *
   * @param request
   *          the servlet request.
   * @return the context URL.
   */
  @Override
  protected String getContextURL(HttpServletRequest request) {
    StringBuilder contextURL = new StringBuilder(getDomainURL(request));
    // the leading slash is already there.
    String contextPath = request.getContextPath();
    if (contextPath != null && contextPath.length() > 0) {
      contextURL.append(contextPath.substring(1));
    }
    return contextURL.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RemoteCallUtils getRemoteCallUtils() {
    return new org.jspresso.framework.qooxdoo.rpc.RemoteCallUtils();
  }
}
