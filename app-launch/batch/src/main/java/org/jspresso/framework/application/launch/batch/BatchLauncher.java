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
package org.jspresso.framework.application.launch.batch;

import org.jspresso.framework.application.startup.batch.IBatchStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Batch launcher.
 *
 * @author Vincent Vandenschrick
 */
public final class BatchLauncher {

  private static final Logger LOG = LoggerFactory.getLogger(BatchLauncher.class);

  private BatchLauncher() {
    // Helper class constructor.
  }

  /**
   * Main method.
   *
   * @param args
   *          arguments.
   */
  public static void main(String... args) {
    try {
      IBatchStartup startup = instanciateStartup(args[0]);
      String[] subArgs = new String[args.length - 1];
      System.arraycopy(args, 1, subArgs, 0, subArgs.length);
      startup.parseCmdLine(subArgs);
      startup.start();
    } catch (InstantiationException | ClassNotFoundException | IllegalAccessException ex) {
      LOG.error("An unexpected error occurred", ex);
      System.exit(1);
    }
  }

  private static IBatchStartup instanciateStartup(String startupClassName)
      throws InstantiationException, IllegalAccessException,
      ClassNotFoundException {
    return (IBatchStartup) Class.forName(startupClassName).newInstance();
  }
}
