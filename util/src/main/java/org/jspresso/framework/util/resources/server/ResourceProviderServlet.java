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
package org.jspresso.framework.util.resources.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.image.ImageHelper;
import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.resources.AbstractResource;
import org.jspresso.framework.util.resources.IActiveResource;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.IResourceBase;
import org.jspresso.framework.util.url.UrlHelper;

/**
 * This servlet class returns the web resource which matches the specified id
 * request parameter requesting it to the resource manager.
 *
 * @author Vincent Vandenschrick
 */
public abstract class ResourceProviderServlet extends HttpServlet {

  private static final long   serialVersionUID             = 5253634459280974738L;

  /**
   * the url pattern to activate a resource download.
   */
  private static final String DOWNLOAD_SERVLET_URL_PATTERN = "/download";

  /**
   * id.
   */
  private static final String ID_PARAMETER                 = "id";

  /**
   * height.
   */
  private static final String IMAGE_HEIGHT_PARAMETER       = "height";

  /**
   * imageUrl.
   */
  private static final String IMAGE_URL_PARAMETER          = "imageUrl";

  /**
   * width.
   */
  private static final String IMAGE_WIDTH_PARAMETER        = "width";

  /**
   * localUrl.
   */
  private static final String LOCAL_URL_PARAMETER          = "localUrl";

  /**
   * omitFileName.
   */
  private static final String OMIT_FILE_NAME_PARAMETER = "omitFileName";

  /**
   * the url pattern to activate a resource upload.
   */
  private static final String UPLOAD_SERVLET_URL_PATTERN   = "/upload";

  /**
   * the regex pattern to match in order to allow the download of a local
   * resource.
   */
  private static final String DEFAULT_LOCAL_URL_REGEX      = "(classpath|http):[A-Za-z0-9_\\-/ ]*\\." +
      "(png|jpg|jpeg|gif|pdf|swf.?)(&width=\\d*+&height=\\d*+)?";

  private static final String ALLOWED_LOCAL_URL_REGEX_KEY  = "allowedLocalUrlRegex";

  private Pattern             allowedLocalUrlPattern       = Pattern
                                                               .compile(
                                                                   DEFAULT_LOCAL_URL_REGEX,
                                                                   Pattern.CASE_INSENSITIVE);

  private static final Logger LOG                          = LoggerFactory
                                                               .getLogger(ResourceProviderServlet.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public void init() {
    String allowedLocalUrlRegex = getInitParameter(ALLOWED_LOCAL_URL_REGEX_KEY);
    if (allowedLocalUrlRegex != null && allowedLocalUrlRegex.length() > 0) {
      allowedLocalUrlPattern = Pattern.compile(allowedLocalUrlRegex,
          Pattern.CASE_INSENSITIVE);
    }
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
   * Computes the url where the image is available for download.
   *
   * @param icon
   *          the icon to load the image for.
   * @param dimension
   *          the requested dimension for the icon if the icon dimension is not
   *          set.
   * @return the resource url.
   */
  public static String computeImageResourceDownloadUrl(Icon icon,
      Dimension dimension) {
    if (icon == null) {
      return null;
    }
    Dimension actualIconSize = dimension;
    if (icon.getDimension() != null) {
      actualIconSize = icon.getDimension();
    }
    return computeImageResourceDownloadUrl(icon.getIconImageURL(),
        actualIconSize);
  }

  /**
   * Computes the url where the image is available for download.
   *
   * @param localImageUrl
   *          the image local url.
   * @param dimension
   *          the requested dimension for the image.
   * @return the resource url.
   */
  public static String computeImageResourceDownloadUrl(String localImageUrl,
      Dimension dimension) {
    if (localImageUrl != null) {
      HttpServletRequest request = HttpRequestHolder.getServletRequest();
      StringBuilder buf = new StringBuilder("?" + IMAGE_URL_PARAMETER + "="
          + localImageUrl);
      if (dimension != null) {
        buf.append("&" + IMAGE_WIDTH_PARAMETER + "=").append(dimension.getWidth());
        buf.append("&" + IMAGE_HEIGHT_PARAMETER + "=").append(dimension.getHeight());
      }
      return computeUrl(request, buf.toString());
    }
    return null;
  }

  /**
   * Computes the url where the resource is available for download.
   *
   * @param localUrl
   *          the resource local url.
   * @return the resource url.
   */
  public static String computeLocalResourceDownloadUrl(String localUrl) {
    return computeLocalResourceDownloadUrl(localUrl, false);
  }

  /**
   * Computes the url where the resource is available for download.
   *
   * @param localUrl
   *          the resource local url.
   * @param omitFileName
   *          when set to true, the file name will not be added as
   *          Content-disposition header in the response. This helps to
   *          workaround security issues in flash SWFLoader.
   * @return the resource url.
   */
  public static String computeLocalResourceDownloadUrl(String localUrl,
      boolean omitFileName) {
    if (localUrl != null) {
      HttpServletRequest request = HttpRequestHolder.getServletRequest();
      return computeUrl(request, "?" + OMIT_FILE_NAME_PARAMETER + "="
          + omitFileName + "&" + LOCAL_URL_PARAMETER + "=" + localUrl);
    }
    return null;
  }

  /**
   * Computes a static URL based on servlet request.
   *
   * @param relativePath
   *          the relative path.
   * @return the absolute static URL.
   */
  public static String computeStaticUrl(String relativePath) {
    return computeStaticUrl(HttpRequestHolder.getServletRequest(), relativePath);
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
   * Computes the url where the resource is available for download.
   *
   * @param request
   *          the incoming HTTP request.
   * @param id
   *          the resource id.
   * @return the resource url.
   */
  private static String computeDownloadUrl(HttpServletRequest request, String id) {
    return computeUrl(request, "?" + ID_PARAMETER + "=" + id);
  }

  /**
   * Computes a static URL based on servlet request.
   *
   * @param request
   *          the servlet request.
   * @param relativePath
   *          the relative path.
   * @return the absolute static URL.
   */
  private static String computeStaticUrl(HttpServletRequest request,
      String relativePath) {
    return request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + request.getContextPath() + "/"
        + relativePath;
  }

  /**
   * Computes the url where the resource can be uploaded.
   *
   * @param request
   *          the incoming HTTP request.
   * @return the resource url.
   */
  private static String computeUploadUrl(HttpServletRequest request) {
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

    try {
      HttpRequestHolder.setServletRequest(request);
      FileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload(factory);

      List<FileItem> items = upload.parseRequest(request);
      response.setContentType("text/xml");
      ServletOutputStream out = response.getOutputStream();
      for (FileItem item : items) {
        if (!item.isFormField()) {
          out.print("<resource");
          IResourceBase uploadResource = new UploadResourceAdapter(
              "application/octet-stream", item);
          String resourceId = ResourceManager.getInstance().register(
              uploadResource);
          out.print(" id=\"" + resourceId);
          out.print("\" name=\"" + HtmlHelper.escapeForHTML(item.getName()));
          out.println("\" />");
        }
      }
      out.flush();
      out.close();
    } catch (Exception ex) {
      LOG.error("An unexpected error occurred while uploading the content.", ex);
    } finally {
      HttpRequestHolder.setServletRequest(null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      HttpRequestHolder.setServletRequest(request);
      String localUrlSpec = request.getParameter(LOCAL_URL_PARAMETER);
      String imageUrlSpec = request.getParameter(IMAGE_URL_PARAMETER);
      String id = request.getParameter(ID_PARAMETER);
      boolean ommitFileName = Boolean.parseBoolean(request
          .getParameter(OMIT_FILE_NAME_PARAMETER));

      if (id == null && localUrlSpec == null && imageUrlSpec == null) {
        throw new ServletException("No resource id nor local URL specified.");
      }

      BufferedInputStream inputStream = null;
      if (id != null) {
        IResourceBase resource = ResourceManager.getInstance()
            .getRegistered(id);
        if (resource == null) {
          throw new ServletException("Bad resource id : " + id);
        }

        response.setContentType(resource.getMimeType());
        if (!ommitFileName) {
          completeFileName(response, resource.getName());
        }
        long resourceLength = resource.getSize();
        if (resourceLength > 0) {
          response.setContentLength((int) resourceLength);
        }

        if (resource instanceof IResource) {
          inputStream = new BufferedInputStream(
              ((IResource) resource).getContent());
        } else if (resource instanceof IActiveResource) {
          OutputStream outputStream = response.getOutputStream();
          try {
            writeActiveResource((IActiveResource) resource, outputStream);
          } catch (RuntimeException ex) {
            try (PrintStream ps = new PrintStream(outputStream)) {
              ex.printStackTrace(new PrintWriter(ps, true));
            }
            throw ex;
          }
        }
      } else if (localUrlSpec != null) {
        if (!UrlHelper.isClasspathUrl(localUrlSpec)) {
          // we must append parameters that are passed AFTER the localUrl
          // parameter as they must be considered as part of the localUrl.
          String queryString = request.getQueryString();
          localUrlSpec = queryString.substring(
              queryString.indexOf(LOCAL_URL_PARAMETER)
                  + LOCAL_URL_PARAMETER.length() + 1, queryString.length());
        }
        if (isLocalUrlAllowed(localUrlSpec)) {
          URL localUrl = UrlHelper.createURL(localUrlSpec);
          if (localUrl == null) {
            throw new ServletException("Bad local URL : " + localUrlSpec);
          }
          if (!ommitFileName) {
            completeFileName(response, localUrl.getFile());
          }
          inputStream = new BufferedInputStream(localUrl.openStream());
        } else {
          LOG.warn(
              "The resource provider servlet filtered a forbidden local URL request ({}). You can adapt the regex "
                  + "security filtering options by modifying the [{}] init parameter on the servlet.",
              localUrlSpec, ALLOWED_LOCAL_URL_REGEX_KEY);
          LOG.warn("Current value is {} = {}", ALLOWED_LOCAL_URL_REGEX_KEY,
              allowedLocalUrlPattern.pattern());
          response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
      } else {
        if (isLocalUrlAllowed(imageUrlSpec)) {
          URL imageUrl = UrlHelper.createURL(imageUrlSpec);
          if (imageUrl == null) {
            throw new ServletException("Bad image URL : " + imageUrlSpec);
          }
          if (!ommitFileName) {
            completeFileName(response, imageUrl.getFile());
          }
          String width = request.getParameter(IMAGE_WIDTH_PARAMETER);
          String height = request.getParameter(IMAGE_HEIGHT_PARAMETER);
          if (width != null && height != null) {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(ImageHelper.scaleImage(imageUrl, Integer.parseInt(width),
                Integer.parseInt(height), "PNG")));
          } else {
            inputStream = new BufferedInputStream(imageUrl.openStream());
          }
        } else {
          LOG.warn(
              "The resource provider servlet filtered a forbidden image URL request ({}). You can adapt the regex "
                  + "security filtering options by modifying the [{}] init parameter on the servlet.",
              imageUrlSpec, ALLOWED_LOCAL_URL_REGEX_KEY);
          LOG.warn("Current value is {} = {}", ALLOWED_LOCAL_URL_REGEX_KEY,
              allowedLocalUrlPattern.pattern());
          response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
      }
      if (inputStream != null) {
        BufferedOutputStream outputStream = new BufferedOutputStream(
            response.getOutputStream());

        IoHelper.copyStream(inputStream, outputStream);

        inputStream.close();
        outputStream.close();
      }
    } catch (ServletException | IOException ex) {
      LOG.error(
          "An exception occurred when dealing with the following request : [{}]",
          request.getRequestURL(), ex);
      try {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      } catch (IOException ioe) {
        throw new NestedRuntimeException(ioe,
            "An exception occurred while sending back a "
                + HttpServletResponse.SC_NOT_FOUND + "error.");
      }
    } finally {
      HttpRequestHolder.setServletRequest(null);
    }
  }

  /**
   * Writes an active resource to the servlet output stream.
   *
   * @param resource
   *          the resource to write.
   * @param outputStream
   *          the servlet outputStream.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  protected void writeActiveResource(IActiveResource resource,
                                     OutputStream outputStream) throws IOException {
    resource.writeToContent(outputStream);
  }

  private void completeFileName(HttpServletResponse response, String fileName) {
    String actualFileName = fileName;
    if (fileName != null && fileName.length() > 0) {
      int pathIndex = fileName.lastIndexOf("/");
      if (pathIndex > 0) {
        actualFileName = fileName.substring(pathIndex + 1);
      }
      response.setHeader("Content-Disposition", "attachment; filename="
          + actualFileName);
    }
  }

  private boolean isLocalUrlAllowed(String localUrl) {
    return localUrl == null
        || allowedLocalUrlPattern.matcher(localUrl).matches();
  }

  private static class UploadResourceAdapter extends AbstractResource {

    private final FileItem item;

    /**
     * Constructs a new {@code UploadResourceAdapter} instance.
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
    public String getName() {
      return item.getName();
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
