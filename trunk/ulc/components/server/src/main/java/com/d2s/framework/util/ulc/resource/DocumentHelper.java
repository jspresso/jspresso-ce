/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.ulc.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import com.d2s.framework.util.io.IoHelper;
import com.d2s.framework.util.resources.IResource;
import com.d2s.framework.util.resources.server.ResourceManager;
import com.d2s.framework.util.resources.server.ResourceProviderServlet;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.container.servlet.application.ServletContainerContext;

/**
 * This class allows to register a resource provider by a unique id and to
 * display the resource provider's content for a given id.
 */
public final class DocumentHelper {

  private static final String APPLICATION_PREFIX = "application/";

  private DocumentHelper() {
    // Helper class constructor.
  }

  /**
   * Shows the document for which a resource entry has been registered.
   * 
   * @param id
   *            the identifier under which the resource has been registered.
   * @throws IOException
   *             whenever an IO exception occurs.
   */
  public static void showDocument(String id) throws IOException {
    showDocument(id, null);
  }

  /**
   * Shows the document for which a resource entry has been registered.
   * 
   * @param id
   *            the identifier under which the resource has been registered.
   * @param target
   *            the target browser id.
   * @throws IOException
   *             whenever an IO exception occurs.
   */
  public static void showDocument(String id, String target) throws IOException {
    if (inDevelopmentEnvironment()) {
      IResource resourceProvider = ResourceManager.getInstance().getRegistered(
          id);
      String fileExtension = ".tmp";
      if (resourceProvider.getMimeType() != null
          && resourceProvider.getMimeType().startsWith(APPLICATION_PREFIX)) {
        fileExtension = "."
            + resourceProvider.getMimeType().substring(
                APPLICATION_PREFIX.length());
      }
      String url = createTemporaryFile(resourceProvider.getContent(),
          fileExtension).toURI().toURL().toString();
      ClientContext.showDocument(url, target);
    } else if (inServletContainerEnvironment()) {
      HttpServletRequest request = ServletContainerContext.getRequest();
      String url = ResourceProviderServlet.computeUrl(request, id);
      ClientContext.showDocument(url, target);
    } else {
      throw new IllegalStateException(
          "Could not determine server runtime environment.");
    }
  }

  private static File createTemporaryFile(InputStream is, String fileExtension)
      throws IOException {
    File file = File.createTempFile(DocumentHelper.class.getName(),
        fileExtension);
    file.deleteOnExit();

    BufferedInputStream inputStream = new BufferedInputStream(is);
    BufferedOutputStream outputStream = new BufferedOutputStream(
        new FileOutputStream(file));

    IoHelper.copyStream(inputStream, outputStream);

    inputStream.close();
    outputStream.close();

    return file;
  }

  private static boolean inDevelopmentEnvironment() {
    try {
      Class.forName("com.ulcjava.base.development.DevelopmentRunner");
      return true;
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
  }

  private static boolean inServletContainerEnvironment() {
    try {
      Class
          .forName("com.ulcjava.container.servlet.application.ServletContainerContext");
      return true;
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
  }
}
