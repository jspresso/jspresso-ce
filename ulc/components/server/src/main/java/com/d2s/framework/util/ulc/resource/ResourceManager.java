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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.d2s.framework.util.io.IoHelper;
import com.d2s.framework.util.resources.IResource;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.container.servlet.application.ServletContainerContext;

/**
 * This class allows to register a resource provider by a unique id and to
 * display the resource provider's content for a given id.
 */
public final class ResourceManager {

  private static final String          DOWNLOAD_SERVLET_URL_PATTERN = "/download";
  private static final ResourceManager INSTANCE                     = new ResourceManager();

  private Map<String, IResource>       resources;
  private SecureRandom                 random;

  private ResourceManager() {
    resources = new HashMap<String, IResource>();
    random = new SecureRandom();
  }

  /**
   * Singleton pattern.
   * 
   * @return the singleton instance.
   */
  public static ResourceManager getInstance() {
    return INSTANCE;
  }

  /**
   * Returns the registered resource or null.
   * 
   * @param id
   *          the identifier under which the resource has been registered.
   * @return the registsred resource or null.
   */
  public IResource getRegistered(String id) {
    return resources.get(id);
  }

  /**
   * Registers a resource.
   * 
   * @param id
   *          the identifier under which the resource must be registered.
   * @param resource
   *          the resource to be registered.
   */
  public void register(String id, IResource resource) {
    resources.put(id, resource);
  }

  /**
   * Registers a resource.
   * 
   * @param resource
   *          the resource to be registered.
   * @return the generated identifier under which the resource has been
   *         registered.
   */
  public String register(IResource resource) {
    try {
      String id = createId();
      resources.put(id, resource);
      return id;
    } catch (NoSuchAlgorithmException nsae) {
      throw new IllegalStateException("Could not generate random id: "
          + nsae.getLocalizedMessage());
    }
  }

  private String createId() throws NoSuchAlgorithmException {
    byte[] bytes = new byte[24];
    random.nextBytes(bytes);
    bytes = MessageDigest.getInstance("MD5").digest(bytes);

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      byte b1 = (byte) ((bytes[i] & 0xf0) >> 4);
      byte b2 = (byte) (bytes[i] & 0x0f);

      result.append(toHex(b1));
      result.append(toHex(b2));
    }

    return result.toString();
  }

  private char toHex(byte b) {
    if (b < 10) {
      return (char) ('0' + b);
    }
    return (char) ('A' + b - 10);
  }

  /**
   * Shows the document for which a resource entry has been registered.
   * 
   * @param id
   *          the identifier under which the resource has been registered.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  public void showDocument(String id) throws IOException {
    showDocument(id, null);
  }

  /**
   * Shows the document for which a resource entry has been registered.
   * 
   * @param id
   *          the identifier under which the resource has been registered.
   * @param target
   *          the target browser id.
   * @throws IOException
   *           whenever an IO exception occurs.
   */
  public void showDocument(String id, String target) throws IOException {
    if (inDevelopmentEnvironment()) {
      IResource resourceProvider = ResourceManager.getInstance().getRegistered(
          id);
      String url = createTemporaryFile(resourceProvider.getContent())
          .toURL().toString();
      ClientContext.showDocument(url, target);
    } else if (inServletContainerEnvironment()) {
      HttpServletRequest request = ServletContainerContext.getRequest();
      String url = determineUrl(request, id);
      ClientContext.showDocument(url, target);
    } else {
      throw new IllegalStateException(
          "Could not determine server runtime environment.");
    }
  }

  private String determineUrl(HttpServletRequest request, String id) {
    String baseUrl = request.getScheme() + "://" + request.getServerName()
        + ":" + request.getServerPort() + request.getContextPath()
        + DOWNLOAD_SERVLET_URL_PATTERN;
    return baseUrl + "?" + ResourceProviderServlet.ID_PARAMETER + "=" + id;
  }

  private boolean inDevelopmentEnvironment() {
    try {
      Class.forName("com.ulcjava.base.development.DevelopmentRunner");
      return true;
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
  }

  private boolean inServletContainerEnvironment() {
    try {
      Class
          .forName("com.ulcjava.container.servlet.application.ServletContainerContext");
      return true;
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
  }

  private File createTemporaryFile(InputStream is) throws IOException {
    File file = File.createTempFile(getClass().getName(), ".tmp");
    file.deleteOnExit();

    BufferedInputStream inputStream = new BufferedInputStream(is);
    BufferedOutputStream outputStream = new BufferedOutputStream(
        new FileOutputStream(file));

    IoHelper.copyStream(inputStream, outputStream);

    inputStream.close();
    outputStream.close();

    return file;
  }
}
