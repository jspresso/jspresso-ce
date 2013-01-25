/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.server.remote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;

/**
 * This servlet class is used to deliver binary content from remote value
 * states.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemotePeerRegistryServlet extends HttpServlet {

  /**
   * id.
   */
  private static final String ID_PARAMETER                 = "id";

  /**
   * PeerRegistry.
   */
  public static final String  PEER_REGISTRY                = "peerRegistry";

  /**
   * the url pattern to activate a resource download.
   */
  private static final String REGISTRY_SERVLET_URL_PATTERN = "/registry";

  private static final long   serialVersionUID             = -2706982900134792757L;

  /**
   * Computes the url where the resource is available for download.
   * 
   * @param request
   *          the incomming HTTP request.
   * @param id
   *          the resource id.
   * @return the resource url.
   */
  public static String computeDownloadUrl(HttpServletRequest request, String id) {
    return computeUrl(request, "?" + ID_PARAMETER + "=" + id);
  }

  /**
   * Computes the url where the resource is available for download.
   * 
   * @param id
   *          the resource id.
   * @return the resource url.
   */
  public static String computeDownloadUrl(String id) {
    HttpServletRequest request = HttpRequestHolder.getServletRequest();
    return computeDownloadUrl(request, id);
  }

  private static String computeUrl(HttpServletRequest request,
      String getParameters) {
    String baseUrl = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort() + request.getContextPath()
        + REGISTRY_SERVLET_URL_PATTERN;
    return baseUrl + getParameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String id = request.getParameter(ID_PARAMETER);

    if (id == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "No state id specified.");
      return;
    }

    BufferedInputStream inputStream;
    IRemotePeerRegistry peerRegistry = (IRemotePeerRegistry) request
        .getSession().getAttribute(PEER_REGISTRY);
    IRemoteStateOwner stateOwner = (IRemoteStateOwner) peerRegistry
        .getRegistered(id);
    byte[] stateValue = (byte[]) stateOwner.actualValue();
    inputStream = new BufferedInputStream(new ByteArrayInputStream(stateValue));
    response.setContentLength(stateValue.length);

    BufferedOutputStream outputStream = new BufferedOutputStream(
        response.getOutputStream());

    IoHelper.copyStream(inputStream, outputStream);

    inputStream.close();
    outputStream.close();
  }
}
