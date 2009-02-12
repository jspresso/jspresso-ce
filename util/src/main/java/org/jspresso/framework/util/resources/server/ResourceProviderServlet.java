/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.resources.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.url.UrlHelper;

/**
 * This servlet class returns the web resource which matches the specified id
 * request parameter requesting it to the resource manager.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ResourceProviderServlet extends HttpServlet {

  /**
   * the url pattern to activate a resource download.
   */
  public static final String DOWNLOAD_SERVLET_URL_PATTERN = "/download";

  /**
   * id.
   */
  public static final String ID_PARAMETER                 = "id";

  /**
   * localUrl.
   */
  public static final String LOCAL_URL_PARAMETER          = "localUrl";

  private static final long  serialVersionUID             = 5253634459280974738L;

  /**
   * Computes the url where the resource is available for download.
   * 
   * @param id
   *          the resource id.
   * @return the rsource url.
   */
  public static String computeUrl(String id) {
    HttpServletRequest request = HttpRequestHolder.getServletRequest();
    return computeUrl(request, id);
  }

  /**
   * Computes the url where the resource is available for download.
   * 
   * @param request
   *          the incomming HTTP request.
   * @param id
   *          the resource id.
   * @return the rsource url.
   */
  public static String computeUrl(HttpServletRequest request, String id) {
    String baseUrl = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort() + request.getContextPath()
        + DOWNLOAD_SERVLET_URL_PATTERN;
    return baseUrl + "?" + ResourceProviderServlet.ID_PARAMETER + "=" + id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String localUrl = request.getParameter(LOCAL_URL_PARAMETER);
    String id = request.getParameter(ID_PARAMETER);

    if (id == null && localUrl == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          "No resource id specified.");
      return;
    }

    BufferedInputStream inputStream;
    if (id != null) {
      IResource resource = ResourceManager.getInstance().getRegistered(id);
      if (resource == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Could not find specified resource id.");
        return;
      }

      response.setContentType(resource.getMimeType());
      int resourceLength = resource.getLength();
      if (resourceLength > 0) {
        response.setContentLength(resourceLength);
      }

      inputStream = new BufferedInputStream(resource.getContent());
    } else {
      inputStream = new BufferedInputStream(UrlHelper.createURL(localUrl)
          .openStream());
    }

    BufferedOutputStream outputStream = new BufferedOutputStream(response
        .getOutputStream());

    IoHelper.copyStream(inputStream, outputStream);

    inputStream.close();
    outputStream.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    doGet(request, response);
  }

}
