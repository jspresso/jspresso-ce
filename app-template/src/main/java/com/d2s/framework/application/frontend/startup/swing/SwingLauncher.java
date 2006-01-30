/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.swing;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.d2s.framework.application.frontend.startup.IStartup;
import com.d2s.framework.util.swing.splash.SplashWindow;
import com.d2s.framework.util.url.UrlHelper;

/**
 * Swing launcher.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class SwingLauncher {

  private SwingLauncher() {
    //Helper class constructor.
  }

  /**
   * Main method.
   * 
   * @param args
   *          arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String[] args) {
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
          .getOptionValue("startupClassName"));
      if (cmd.hasOption("splash")) {
        SplashWindow.splash(UrlHelper.createURL(cmd.getOptionValue("splash"),
            ClassLoader.getSystemClassLoader()));
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
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return (IStartup) Class.forName(startupClassName).newInstance();
  }
}
