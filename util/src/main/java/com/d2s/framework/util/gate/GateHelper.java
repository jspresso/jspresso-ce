/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.gate;

import java.util.Collection;

/**
 * This helper class contains utility methods to work with gates.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class GateHelper {

  private GateHelper() {
    // Just here to prevent direct instanciation.
  }

  /**
   * Returns true if and only if gates are null, empty or all open.
   * 
   * @param gates
   *            the gates collection.
   * @return gates status.
   */
  public static boolean areGatesOpen(Collection<IGate> gates) {
    if (gates != null) {
      for (IGate gate : gates) {
        if (!gate.isOpen()) {
          return false;
        }
      }
    }
    return true;
  }
}
