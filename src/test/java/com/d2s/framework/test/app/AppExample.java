/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.app;

import java.util.Locale;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.IFrontendController;
import com.d2s.framework.sample.data.AppDataProducer;
import com.d2s.framework.test.model.AbstractModelTest;
import com.d2s.framework.util.swing.splash.SplashWindow;
import com.d2s.framework.util.url.UrlHelper;
import com.d2s.framework.view.projection.BeanProjection;
import com.d2s.framework.view.projection.Projection;

/**
 * Swing view testing.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AppExample extends AbstractModelTest {

  /**
   * Tests Swing view construction.
   */
  private void start() {
    Locale locale = Locale.FRENCH;

    IFrontendController frontController = (IFrontendController) getApplicationContext()
        .getBean("testFrontController");

    IBackendController backController = (IBackendController) getApplicationContext()
        .getBean("testBackController");
    BeanProjection companyProjection = (BeanProjection) ((Projection) backController
        .getRootProjectionConnector("company").getConnectorValue())
        .getChildren().get(0);
    companyProjection.setProjectedObjects(new AppDataProducer(
        getApplicationContext()).createTestData());
    frontController.start(backController, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "com.d2s.framework.sample.frontend";
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
    CommandLineParser parser = new BasicParser();
    boolean splashed = false;
    try {
      CommandLine cmd = parser.parse(options, args);
      if (cmd.hasOption("splash")) {
        SplashWindow.splash(UrlHelper.createURL(cmd.getOptionValue("splash"),
            ClassLoader.getSystemClassLoader()));
        splashed = true;
      }
    } catch (ParseException ex) {
      System.err.println(ex.getLocalizedMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(AppExample.class.getSimpleName(), options);
    }
    AppExample app = new AppExample();
    try {
      app.setUp();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    app.start();
    if (splashed) {
      SplashWindow.disposeSplash();
    }
  }
}
