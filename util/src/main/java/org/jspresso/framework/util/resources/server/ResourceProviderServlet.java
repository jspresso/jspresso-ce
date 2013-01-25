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
package org.jspresso.framework.util.resources.server;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.io.IoHelper;
import org.jspresso.framework.util.resources.AbstractResource;
import org.jspresso.framework.util.resources.IActiveResource;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.IResourceBase;
import org.jspresso.framework.util.url.UrlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet class returns the web resource which matches the specified id
 * request parameter requesting it to the resource manager.
 * 
 * @version $LastChangedRevision$
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
   * ommitFileName.
   */
  private static final String OMMIT_FILE_NAME_PARAMETER    = "ommitFileName";

  /**
   * the url pattern to activate a resource upload.
   */
  private static final String UPLOAD_SERVLET_URL_PATTERN   = "/upload";

  /**
   * the regex pattern to match in order to allow the download of a local
   * resource.
   */
  private static final String DEFAULT_LOCAL_URL_REGEX      = "(classpath|http):[A-Za-z0-9_\\-/ ]*\\.(png|jpg|jpeg|gif|pdf|swf.?)";

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
   * @param ommitFileName
   *          when set to true, the file name will not be added as
   *          Content-disposition header in the response. This helps to
   *          workaround scurity issues in flash SWFLoader.
   * @return the resource url.
   */
  public static String computeLocalResourceDownloadUrl(String localUrl,
      boolean ommitFileName) {
    if (localUrl != null) {
      HttpServletRequest request = HttpRequestHolder.getServletRequest();
      return computeUrl(request, "?" + OMMIT_FILE_NAME_PARAMETER + "="
          + ommitFileName + "&" + LOCAL_URL_PARAMETER + "=" + localUrl);
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
   *          the incomming HTTP request.
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
      LOG.error("An unexpected error occured while uploading the content.", ex);
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
          .getParameter(OMMIT_FILE_NAME_PARAMETER));

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
          writeActiveResource((IActiveResource) resource, response);
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
      } else if (imageUrlSpec != null) {
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
            inputStream = scaleImage(imageUrl, Integer.parseInt(width),
                Integer.parseInt(height));
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
    } catch (ServletException sex) {
      LOG.error(
          "An exception occurred when dealing with the following request : [{}]",
          request.getRequestURL(), sex);
      try {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      } catch (IOException ex) {
        throw new NestedRuntimeException(ex,
            "An exception occured while sending back a "
                + HttpServletResponse.SC_NOT_FOUND + "error.");
      }
    } catch (IOException ioex) {
      LOG.error(
          "An exception occurred when dealing with the following request : [{}]",
          request.getRequestURL(), ioex);
      try {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      } catch (IOException ex) {
        throw new NestedRuntimeException(ex,
            "An exception occured while sending back a "
                + HttpServletResponse.SC_NOT_FOUND + "error.");
      }
    } finally {
      HttpRequestHolder.setServletRequest(null);
    }
  }

  /**
   * Writes an active resource to the servlet outputstream.
   * 
   * @param resource
   *          the resource to write.
   * @param response
   *          the servlet response.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  protected void writeActiveResource(IActiveResource resource,
      HttpServletResponse response) throws IOException {
    OutputStream outputStream = response.getOutputStream();
    resource.writeToContent(outputStream);
    outputStream.flush();
    outputStream.close();
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

  private BufferedInputStream scaleImage(URL originalImageUrl, int width,
      int height) throws IOException {
    BufferedImage image = ImageIO.read(originalImageUrl);
    BufferedImage scaledImage = getScaledInstance(image, width, height,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(scaledImage, "PNG", baos);
    return new BufferedInputStream(new ByteArrayInputStream(baos.toByteArray()));
  }

  /**
   * Convenience method that returns a scaled instance of the provided
   * {@code BufferedImage}.
   * 
   * @param img
   *          the original image to be scaled
   * @param targetWidth
   *          the desired width of the scaled instance, in pixels
   * @param targetHeight
   *          the desired height of the scaled instance, in pixels
   * @param hint
   *          one of the rendering hints that corresponds to
   *          {@code RenderingHints.KEY_INTERPOLATION} (e.g.
   *          {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
   *          {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
   *          {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
   * @param higherQuality
   *          if true, this method will use a multi-step scaling technique that
   *          provides higher quality than the usual one-step technique (only
   *          useful in downscaling cases, where {@code targetWidth} or
   *          {@code targetHeight} is smaller than the original dimensions, and
   *          generally only when the {@code BILINEAR} hint is specified)
   * @return a scaled version of the original {@code BufferedImage}
   */
  public BufferedImage getScaledInstance(BufferedImage img, int targetWidth,
      int targetHeight, Object hint, boolean higherQuality) {
    int type = BufferedImage.TYPE_INT_ARGB;
    if (img.getTransparency() == Transparency.OPAQUE) {
      type = BufferedImage.TYPE_INT_RGB;
    }
    BufferedImage ret = img;
    int w, h;
    if (higherQuality) {
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      w = img.getWidth();
      h = img.getHeight();
    } else {
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }

    do {
      if (higherQuality && w > targetWidth) {
        w /= 2;
      }
      if (w < targetWidth) {
        w = targetWidth;
      }

      if (higherQuality && h > targetHeight) {
        h /= 2;
      }
      if (h < targetHeight) {
        h = targetHeight;
      }

      BufferedImage tmp = new BufferedImage(w, h, type);
      Graphics2D g2 = tmp.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
      g2.drawImage(ret, 0, 0, w, h, null);
      g2.dispose();

      ret = tmp;
    } while (w != targetWidth || h != targetHeight);

    return ret;
  }

  private boolean isLocalUrlAllowed(String localUrl) {
    return localUrl == null
        || allowedLocalUrlPattern.matcher(localUrl).matches();
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
