/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jspresso.framework.application.startup.IStartup;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.util.swing.splash.SplashWindow;
import org.jspresso.framework.util.url.UrlHelper;


/**
 * Swing launcher.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class SwingLauncher {

  private SwingLauncher() {
    // Helper class constructor.
  }

  /**
   * Main method.
   * 
   * @param args
   *            arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String[] args) {
    SwingUtil.installDefaults();
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName("splash").hasArg()
        .withDescription(
            "use given image URL for splash (Supports classpath: pseudo URLs)")
        .create("splash"));
    options.addOption(OptionBuilder.withArgName("applicationClass").hasArg()
        .withDescription("use given class name as startup class.").isRequired()
        .create("applicationClass"));
    CommandLineParser parser = new BasicParser();
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
    } catch (ParseException ex) {
      System.err.println(ex.getLocalizedMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(SwingLauncher.class.getSimpleName(), options);
    } catch (InstantiationException ex) {
      ex.printStackTrace(System.err);
    } catch (IllegalAccessException ex) {
      ex.printStackTrace(System.err);
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace(System.err);
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
