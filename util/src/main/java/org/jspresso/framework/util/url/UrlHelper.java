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
package org.jspresso.framework.util.url;

import java.net.MalformedURLException;
import java.net.URL;

import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * This is a simple helper class to be able to cope with "classpath:" urls.
 *
 * @author Vincent Vandenschrick
 */
public final class UrlHelper {

  private static final String CLASSPATH_URL     = "classpath:";
  private static final String JAR_URL           = "jar:";
  private static final String JAR_URL_SEPARATOR = "!/";
  /**
   * The constant BLANK_TARGET is &quot;_blank&quot;.
   */
  public static final  String BLANK_TARGET      = "_blank";
  /**
   * The constant SELF_TARGET is &quot;_self&quot;.
   */
  public static final  String SELF_TARGET       = "_self";

  private UrlHelper() {
    // private constructor for helper class.
  }

  /**
   * Creates a URL object.
   *
   * @param urlSpec
   *          the string representation of the URL. In case of a classpath url
   *          the thread context classloader will be used.
   * @return the constructed URL or null.
   */
  public static URL createURL(String urlSpec) {
    return createURL(urlSpec, Thread.currentThread().getContextClassLoader());
  }

  /**
   * Creates a URL object.
   *
   * @param urlSpec
   *          the string representation of the URL.
   * @param cl
   *          the class loader used to get the resource URL in case of a
   *          "classpath:" URL.
   * @return the constructed URL or null.
   */
  public static URL createURL(String urlSpec, ClassLoader cl) {
    if (urlSpec == null) {
      return null;
    }
    URL returnedURL;
    if (urlSpec.startsWith(JAR_URL) && urlSpec.indexOf(CLASSPATH_URL) > 0) {
      String entryPath = urlSpec.split(JAR_URL_SEPARATOR)[1];
      URL jarFileUrl = createURL(urlSpec.substring(JAR_URL.length(), urlSpec.indexOf(JAR_URL_SEPARATOR)), cl);
      try {
        String spec = JAR_URL + jarFileUrl.toString() + JAR_URL_SEPARATOR
            + entryPath;
        returnedURL = new URL(spec);
      } catch (MalformedURLException ex) {
        throw new NestedRuntimeException(ex);
      }
    } else if (isClasspathUrl(urlSpec)) {
      String resourcePath = getResourcePathOrUrl(urlSpec, false);
      returnedURL = cl.getResource(resourcePath);
    } else {
      try {
        returnedURL = new URL(urlSpec);
      } catch (MalformedURLException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    return returnedURL;
  }

  /**
   * Extracts the resource path from a "classpath:" URL or return the original
   * one if it is not a "classpath:" URL.
   *
   * @param urlSpec
   *          the string representation of the URL.
   * @param makeAbsolute
   *          if true, ensure that resource path are made absolute.
   * @return the extracted resource path or the original URL.
   */
  public static String getResourcePathOrUrl(String urlSpec, boolean makeAbsolute) {
    if (isClasspathUrl(urlSpec)) {
      String resourcePath = urlSpec.substring(CLASSPATH_URL.length());
      int paramIndex = resourcePath.indexOf("?");
      if (paramIndex > 0) {
        resourcePath = resourcePath.substring(0, paramIndex);
      }
      if (makeAbsolute) {
        if (!resourcePath.startsWith("/")) {
          resourcePath = "/" + resourcePath;
        }
      } else {
        while (resourcePath.startsWith("/")) {
          resourcePath = resourcePath.substring(1);
        }
      }
      return resourcePath;
    }
    return urlSpec;
  }

  /**
   * Whether this url is a "classpath:" pseudo URL.
   *
   * @param urlSpec
   *          the string representation of the URL.
   * @return true if this url is a "classpath:" pseudo URL.
   */
  public static boolean isClasspathUrl(String urlSpec) {
    return urlSpec.startsWith(CLASSPATH_URL);
  }
}
