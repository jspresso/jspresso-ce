package com.d2s.framework.util.lang;

/**
 * Helper class for objects operations.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ObjectUtils {

  /**
   * Compares two objects for equality.
   * 
   * @param object1
   *          the first object, may be <code>null</code>
   * @param object2
   *          the second object, may be <code>null</code>
   * @return <code>true</code> if the values of both objects are the same
   */
  public static boolean equals(Object object1, Object object2) {
    if (object1 == object2) {
      return true;
    }
    if ((object1 == null) || (object2 == null)) {
      return false;
    }
    return object1.equals(object2);
  }

  private ObjectUtils() {
    // Helper class constructor
  }
}
