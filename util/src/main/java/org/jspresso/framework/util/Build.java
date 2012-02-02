/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Jspresso version.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class Build {

  private static Properties   jspressoProperties;

  static {
    jspressoProperties = new Properties();
    try {
      jspressoProperties.load(Build.class
          .getResourceAsStream("/META-INF/jspresso.properties"));
    } catch (IOException ioe) {
      System.err.println(
          "Version.properties could not be loaded from the current classloader.");
      ioe.printStackTrace(System.err);
    }
  }

  private Build() {
    // Private helper constructor.
  }

  /**
   * Retrieves the Jspresso version.
   * 
   * @return the Jspresso version.
   */
  public static String getJspressoVersion() {
    return jspressoProperties.getProperty("jspresso.version", "UNKNOWN");
  }
  
  /**
   * Displays various informations about the framework version.
   * 
   * @param args none supported.
   */
  public static void main(String[] args) {
    System.out.println("Jspresso version : " + getJspressoVersion());
  }
}
