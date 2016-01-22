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
package org.jspresso.framework.application.startup.remote;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A simple servlet to receive browser disconnect commands.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteUtilServlet extends HttpServlet {

  private static final long   serialVersionUID         = -3666419534888301379L;

  /**
   * {@code REMOTE_STARTUP}.
   */
  public static final String  REMOTE_STARTUP           = "remoteStartup";

  /**
   * the url pattern to stop the session remote startup.
   */
  private static final String STOP_SERVLET_URL_PATTERN = "/stop";

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    resp.setHeader("Cache-control", "no-cache, no-store");
    resp.setHeader("Pragma", "no-cache");
    resp.setHeader("Expires", "-1");
    HttpSession session = req.getSession();
    if (req.getPathInfo() != null
        && req.getPathInfo().endsWith(STOP_SERVLET_URL_PATTERN)
        && session != null) {
      RemoteStartup startup = (RemoteStartup) session.getAttribute(
          REMOTE_STARTUP);
      if (startup != null) {
        startup.stop();
      }
      session.invalidate();
    }
  }
}
