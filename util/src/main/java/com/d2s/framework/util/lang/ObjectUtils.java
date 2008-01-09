package com.d2s.framework.util.lang;

/**
 * Helper class for objects operations.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ObjectUtils {

  private ObjectUtils() {
    // Helper class constructor
  }

  /**
   * Compares two objects for equality.
   * 
   * @param object1
   *            the first object, may be <code>null</code>
   * @param object2
   *            the second object, may be <code>null</code>
   * @return <code>true</code> if the values of both objects are the same
   */
  public static boolean equals(Object object1, Object object2) {
    if (object1 == object2) {
      return true;
    }
    if ((object1 == null) || (object2 == null)) {
      return false;
    }
    if (object1 instanceof Number && object2 instanceof Number) {
      return ((Number) object1).doubleValue() == ((Number) object2)
          .doubleValue();
    }
    return object1.equals(object2);
  }
}
