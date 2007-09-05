/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util;

/**
 * Implementations of this interface are designed to provide image urls based on
 * an object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IIconImageURLProvider {

  /**
   * Gets the image url for the user object passed as parameter.
   * 
   * @param userObject
   *            the user object to get the image url for.
   * @return the looked up image url or null if none.
   */
  String getIconImageURLForObject(Object userObject);
}
