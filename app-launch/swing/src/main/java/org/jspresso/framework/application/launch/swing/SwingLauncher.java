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
package org.jspresso.framework.application.launch.swing;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.application.startup.IStartup;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.util.swing.splash.SplashWindow;
import org.jspresso.framework.util.url.UrlHelper;

/**
 * Swing launcher.
 *
 * @author Vincent Vandenschrick
 */
public final class SwingLauncher {

  private static final Logger LOG = LoggerFactory.getLogger(SwingLauncher.class);

  private SwingLauncher() {
    // Helper class constructor.
  }

  /**
   * Main method.
   *
   * @param args
   *          arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String... args) {
    NativeInterface.open();
    SwingUtil.installDefaults();
    Options options = new Options();
    options.addOption(Option.builder("splash")
        .argName("splash")
        .hasArg()
        .desc(
            "use given image URL for splash (Supports classpath: pseudo URLs)")
        .build());
    options.addOption(Option.builder("applicationClass").argName("applicationClass").hasArg()
        .desc("use given class name as startup class.").required()
        .build());
    CommandLineParser parser = new DefaultParser();
    boolean splashed = false;
    try {
      CommandLine cmd = parser.parse(options, args);
      IStartup startup = instanciateStartup(cmd
          .getOptionValue("applicationClass"));
      if (cmd.hasOption("splash")) {
        SplashWindow.splash(UrlHelper.createURL(cmd.getOptionValue("splash")));
        splashed = true;
      }
      startup.start();
      NativeInterface.runEventPump();
    } catch (ParseException ex) {
      LOG.error("Couldn't parse the command line", ex);
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(SwingLauncher.class.getSimpleName(), options);
    } catch (InstantiationException | ClassNotFoundException | IllegalAccessException ex) {
      LOG.error("An unexpected error occurred.", ex);
    }
    if (splashed) {
      SplashWindow.disposeSplash();
    }
  }

  private static IStartup instanciateStartup(String startupClassName)
      throws InstantiationException, IllegalAccessException,
      ClassNotFoundException {
    return (IStartup) Class.forName(startupClassName).newInstance();
  }
}
