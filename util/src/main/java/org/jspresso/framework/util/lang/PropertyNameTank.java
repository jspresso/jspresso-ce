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
package org.jspresso.framework.util.lang;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a simple singleton to hold a finished mapping of string to int.
 */
public final class PropertyNameTank {

  private static final ConcurrentHashMap<String, Integer> TANK = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<Integer, String> KNAT = new ConcurrentHashMap<>();

  private PropertyNameTank() {
    // HelperConstructor
  }

  /**
   * Retrieves the integer mapping of a given property name.
   *
   * @param propertyName the property name
   * @return the int
   */
  public static int indexOf(String propertyName) {
    Integer index = TANK.get(propertyName);
    if (index != null) {
      return index;
    }
    index = feedTank(propertyName);
    return index;
  }

  private static Integer feedTank(String propertyName) {
    Integer index;
    synchronized (TANK) {
      index = TANK.get(propertyName);
      // Now that we acquire the lock, re-check that index is still null
      if (index == null) {
        index = TANK.size();
        TANK.put(propertyName, index);
        KNAT.put(index, propertyName);
      }
    }
    return index;
  }

  /**
   * Retrieves the property name mapping of a given index.
   *
   * @param index the index.
   * @return the property name.
   */
  public static String nameOf(int index) {
    String propertyName = KNAT.get(index);
    return propertyName;
  }
}
