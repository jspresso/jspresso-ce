/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.lang;

import java.util.Comparator;

/**
 * Compares two classes based on their inheritance with each other.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ClassInheritanceComparator implements Comparator<Class<?>> {

  /**
   * If clazz1 inherits from clazz2 but is different, then clazz1 is greater
   * than clazz2. If clazz2 inherits from clazz1 but is different, then clazz2
   * is greater than clazz1. clazz1 is compared to clazz2 through their names in
   * any other case to enforce transitivity of the comparison.
   * <p>
   * {@inheritDoc}
   */
  public int compare(Class<?> clazz1, Class<?> clazz2) {
    if (clazz1.equals(clazz2)) {
      return 0;
    } else if (clazz1.isAssignableFrom(clazz2)) {
      return 1;
    } else if (clazz2.isAssignableFrom(clazz1)) {
      return -1;
    }
    return clazz1.getName().compareTo(clazz2.getName());
  }

}
