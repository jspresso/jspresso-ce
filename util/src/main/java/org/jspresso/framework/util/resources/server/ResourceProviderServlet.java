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
package org.jspresso.framework.util.resources.server;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.resources.AbstractResource;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.url.UrlHelper;

/**
 * This servlet class returns the web resource which matches the specified id
 * request parameter requesting it to the resource manager.
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
   * imageUrl.
   */
  private static final String IMAGE_URL_PARAMETER          = "imageUrl";

  /**
   * width.
   */
  private static final String IMAGE_WIDTH_PARAMETER        = "width";

  /**
   * height.
   */
  private static final String IMAGE_HEIGHT_PARAMETER       = "height";

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
  private static String computeDownloadUrl(HttpServletRequest request, String id) {
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
      StringBuffer buf = new StringBuffer("?" + IMAGE_URL_PARAMETER + "="
          + localImageUrl);
      if (dimension != null) {
        buf.append("&" + IMAGE_WIDTH_PARAMETER + "=" + dimension.getWidth());
        buf.append("&" + IMAGE_HEIGHT_PARAMETER + "=" + dimension.getHeight());
      }
      return computeUrl(request, buf.toString());
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
      throws IOException, ServletException {
    String localUrlSpec = request.getParameter(LOCAL_URL_PARAMETER);
    String imageUrlSpec = request.getParameter(IMAGE_URL_PARAMETER);
    String id = request.getParameter(ID_PARAMETER);

    if (id == null && localUrlSpec == null && imageUrlSpec == null) {
      throw new ServletException("No resource id nor local URL specified.");
    }

    BufferedInputStream inputStream = null;
    if (id != null) {
      IResource resource = ResourceManager.getInstance().getRegistered(id);
      if (resource == null) {
        throw new ServletException("Bad resource id : " + id);
      }

      response.setContentType(resource.getMimeType());
      long resourceLength = resource.getSize();
      if (resourceLength > 0) {
        response.setContentLength((int) resourceLength);
      }

      inputStream = new BufferedInputStream(resource.getContent());
    } else if (localUrlSpec != null) {
      if (!UrlHelper.isClasspathUrl(localUrlSpec)) {
        // we must append parameters that are passed AFTER the localUrl
        // parameter as they must be considered as part of the localUrl.
        String queryString = request.getQueryString();
        localUrlSpec = queryString.substring(queryString
            .indexOf(LOCAL_URL_PARAMETER)
            + LOCAL_URL_PARAMETER.length() + 1, queryString.length());
      }
      URL localUrl = UrlHelper.createURL(localUrlSpec);
      if (localUrl == null) {
        throw new ServletException("Bad local URL : " + localUrlSpec);
      }
      inputStream = new BufferedInputStream(localUrl.openStream());
    } else if (imageUrlSpec != null) {
      URL imageUrl = UrlHelper.createURL(imageUrlSpec);
      if (imageUrl == null) {
        throw new ServletException("Bad image URL : " + imageUrlSpec);
      }
      String width = request.getParameter(IMAGE_WIDTH_PARAMETER);
      String height = request.getParameter(IMAGE_HEIGHT_PARAMETER);
      if (width != null && height != null) {
        inputStream = scaleImage(imageUrl, Integer.parseInt(width), Integer
            .parseInt(height));
      } else {
        inputStream = new BufferedInputStream(imageUrl.openStream());
      }
    }
    if (inputStream != null) {
      BufferedOutputStream outputStream = new BufferedOutputStream(response
          .getOutputStream());

      IoHelper.copyStream(inputStream, outputStream);

      inputStream.close();
      outputStream.close();
    }
  }

  private BufferedInputStream scaleImage(URL originalImageUrl, int width,
      int height) throws IOException {
    BufferedImage image = ImageIO.read(originalImageUrl);
    BufferedImage scaledImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = scaledImage.createGraphics();
    AffineTransform at = AffineTransform.getScaleInstance((double) width
        / image.getWidth(), (double) height / image.getHeight());
    g.drawRenderedImage(image, at);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(scaledImage, "PNG", baos);
    return new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
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
    public InputStream getContent() throws IOException {
      return item.getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    public long getSize() {
      return item.getSize();
    }

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
}
