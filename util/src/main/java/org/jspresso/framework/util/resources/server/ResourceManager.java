/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.resources.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.util.resources.IResource;


/**
 * This class allows to register a resource provider by a unique id and to
 * display the resource provider's content for a given id.
 */
public final class ResourceManager {

  private static final ResourceManager INSTANCE = new ResourceManager();

  private SecureRandom                 random;
  private Map<String, IResource>       resources;

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
   *            the identifier under which the resource has been registered.
   * @return the registsred resource or null.
   */
  public IResource getRegistered(String id) {
    return resources.get(id);
  }

  /**
   * Registers a resource.
   * 
   * @param resource
   *            the resource to be registered.
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

  /**
   * Registers a resource.
   * 
   * @param id
   *            the identifier under which the resource must be registered.
   * @param resource
   *            the resource to be registered.
   */
  public void register(String id, IResource resource) {
    resources.put(id, resource);
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
}
