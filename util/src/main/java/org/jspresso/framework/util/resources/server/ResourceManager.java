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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;

import org.jspresso.framework.util.resources.IResourceBase;

/**
 * This class allows to register a resource provider by a unique id and to
 * display the resource provider's content for a given id. Once a resource has
 * been taken out of the resource manager, it is automatically unregistered. So
 * resource storage in resource manager is a one-time usage.
 */
public final class ResourceManager {

  private static final ResourceManager INSTANCE = new ResourceManager();

  private final SecureRandom               random;
  private final Map<String, IResourceBase> resources;

  @SuppressWarnings("unchecked")
  private ResourceManager() {
    // resources = new HashMap<String, IResource>();
    resources = new ReferenceMap(AbstractReferenceMap.ReferenceStrength.SOFT,
        AbstractReferenceMap.ReferenceStrength.SOFT, true);
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
   *     the identifier under which the resource has been registered.
   * @return the registered resource or null.
   */
  public IResourceBase getRegistered(String id) {
    IResourceBase resource = resources.get(id);
    // Do not un-register resource once retrieved. There might be cases
    // when the resource must be retrieved multiple times.
    // un-register(id);
    return resource;
  }

  /**
   * Registers a resource.
   *
   * @param resource
   *     the resource to be registered.
   * @return the generated identifier under which the resource has been
   * registered.
   */
  public String register(IResourceBase resource) {
    try {
      String id = createId();
      resources.put(id, resource);
      return id;
    } catch (NoSuchAlgorithmException nsae) {
      throw new IllegalStateException("Could not generate random id: " + nsae.getLocalizedMessage());
    }
  }

  /**
   * Registers a resource.
   *
   * @param id
   *     the identifier under which the resource must be registered.
   * @param resource
   *     the resource to be registered.
   */
  public void register(String id, IResourceBase resource) {
    resources.put(id, resource);
  }

  /**
   * Un-registers a resource.
   *
   * @param id
   *     the identifier under which the resource is registered.
   */
  public void unregister(String id) {
    resources.remove(id);
  }

  private String createId() throws NoSuchAlgorithmException {
    byte[] bytes = new byte[24];
    random.nextBytes(bytes);
    bytes = MessageDigest.getInstance("MD5").digest(bytes);

    StringBuilder result = new StringBuilder();
    for (byte aByte : bytes) {
      byte b1 = (byte) ((aByte & 0xf0) >> 4);
      byte b2 = (byte) (aByte & 0x0f);

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
