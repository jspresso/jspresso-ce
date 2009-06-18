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
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.resources.AbstractResource;
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
  private static final String DOWNLOAD_SERVLET_URL_PATTERN = "/download";

  /**
   * id.
   */
  private static final String ID_PARAMETER                 = "id";

  /**
   * localUrl.
   */
  private static final String LOCAL_URL_PARAMETER          = "localUrl";

  /**
   * the url pattern to activate a resource upload.
   */
  private static final String UPLOAD_SERVLET_URL_PATTERN   = "/upload";

  private static final long   serialVersionUID             = 5253634459280974738L;

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

  /**
   * Computes the url where the resource is available for download.
   * 
   * @param localUrl
   *          the resource local url.
   * @return the resource url.
   */
  public static String computeLocalResourceDownloadUrl(String localUrl) {
    if (localUrl != null) {
      HttpServletRequest request = HttpRequestHolder.getServletRequest();
      return computeUrl(request, "?" + LOCAL_URL_PARAMETER + "=" + localUrl);
    }
    return null;
  }

  /**
   * Computes the url where the resource can be uploaded.
   * 
   * @return the resource url.
   */
  public static String computeUploadUrl() {
    HttpServletRequest request = HttpRequestHolder.getServletRequest();
    return computeUploadUrl(request);
  }

  /**
   * Computes the url where the resource can be uploaded.
   * 
   * @param request
   *          the incomming HTTP request.
   * @return the resource url.
   */
  public static String computeUploadUrl(HttpServletRequest request) {
    String baseUrl = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort() + request.getContextPath()
        + UPLOAD_SERVLET_URL_PATTERN;
    return baseUrl;
  }

  private static String computeUrl(HttpServletRequest request,
      String getParameters) {
    String baseUrl = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort() + request.getContextPath()
        + DOWNLOAD_SERVLET_URL_PATTERN;
    return baseUrl + getParameters;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) {

    ServletOutputStream out = null;

    try {
      FileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload(factory);

      List<FileItem> items = upload.parseRequest(request);
      response.setContentType("text/xml");
      out = response.getOutputStream();
      for (FileItem item : items) {
        if (!item.isFormField()) {
          out.print("<resource");
          IResource uploadResource = new UploadResourceAdapter(
              "application/octet-stream", item);
          String resourceId = ResourceManager.getInstance().register(
              uploadResource);
          out.print(" id=\"" + resourceId);
          out.print("\" name=\"" + item.getName());
          out.println("\" />");
        }
      }
      out.flush();
      out.close();
    } catch (FileUploadException fue) {
      fue.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
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
      long resourceLength = resource.getSize();
      if (resourceLength > 0) {
        response.setContentLength((int) resourceLength);
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

  private static class UploadResourceAdapter extends AbstractResource {

    private FileItem item;

    /**
     * Constructs a new <code>UploadResourceAdapter</code> instance.
     * 
     * @param mimeType
     *          the resource mime type.
     * @param item
     *          the resource file item.
     */
    public UploadResourceAdapter(String mimeType, FileItem item) {
      super(mimeType);
      this.item = item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getContent() throws IOException {
      return item.getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSize() {
      return item.getSize();
    }

  }
}
