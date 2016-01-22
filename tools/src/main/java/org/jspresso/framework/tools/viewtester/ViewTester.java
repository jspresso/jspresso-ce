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
package org.jspresso.framework.tools.viewtester;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Generates Jspresso powered component java code based on its descriptor.
 *
 * @author Vincent Vandenschrick
 */
public class ViewTester {

  private static final Logger LOG                     = LoggerFactory
                                                           .getLogger(ViewTester.class);
  private static final String BEAN_FACTORY_SELECTOR   = "beanFactorySelector";
  private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
  private static final String LANGUAGE                = "language";
  private static final String VIEW_ID                 = "viewId";

  private String              beanFactorySelector;
  private String              applicationContextKey;
  private String              language;
  private String              viewId;

  /**
   * Starts Code generation for an component.
   *
   * @param args
   *          the command line arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String... args) {
    Options options = new Options();
    options
        .addOption(Option.builder(APPLICATION_CONTEXT_KEY)
            .argName(APPLICATION_CONTEXT_KEY)
            .required()
            .hasArg()
            .desc(
                "use given applicationContextKey as registered in the Spring BeanFactoryLocator.")
            .build());
    options
        .addOption(Option.builder(BEAN_FACTORY_SELECTOR)
            .argName(BEAN_FACTORY_SELECTOR)
            .hasArg()
            .desc(
                "use given resource path to lookup the Spring BeanFactoryLocator. If not set, defaults to beanRefFactory.xml")
            .build());
    options.addOption(Option.builder(VIEW_ID)
        .argName(VIEW_ID)
        .required()
        .hasArg()
        .desc(
            "use given view identifier to instantiate and display the view.")
        .build());
    options.addOption(Option.builder(LANGUAGE)
        .argName(LANGUAGE)
        .hasArg()
        .desc(
            "use given locale to instantiate and display the view.")
        .build());
    CommandLineParser parser = new BasicParser();
    CommandLine cmd;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException ex) {
      LOG.error("Error parsing command line", ex);
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(ViewTester.class.getSimpleName(), options);
      return;
    }

    final ViewTester tester = new ViewTester();
    tester.setBeanFactorySelector(cmd.getOptionValue(BEAN_FACTORY_SELECTOR));
    tester
        .setApplicationContextKey(cmd.getOptionValue(APPLICATION_CONTEXT_KEY));
    tester.setViewId(cmd.getOptionValue(VIEW_ID));
    tester.setLanguage(cmd.getOptionValue(LANGUAGE));
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        tester.displayView();
      }
    });
  }

  /**
   * Generates the component java source files.
   */
  @SuppressWarnings("unchecked")
  public void displayView() {
    Locale locale;
    if (language != null) {
      locale = LocaleUtils.toLocale(language);
    } else {
      locale = Locale.getDefault();
    }

    ApplicationContext appContext = getApplicationContext();
    IViewDescriptor viewDescriptor = (IViewDescriptor) appContext
        .getBean(viewId);

    IFrontendController<JComponent, Icon, Action> mockFrontController = (IFrontendController<JComponent, Icon, Action>) appContext
        .getBean("applicationFrontController");
    IBackendController mockBackController = (IBackendController) appContext
        .getBean("applicationBackController");
    BackendControllerHolder.setSessionBackendController(mockBackController);

    mockFrontController
        .start(mockBackController, locale, TimeZone.getDefault());

    IView<JComponent> view = mockFrontController.getViewFactory().createView(
        viewDescriptor, mockFrontController, locale);

    if (viewDescriptor.getModelDescriptor() != null) {
      IValueConnector modelConnector = mockBackController.createModelConnector(
          IValueConnector.MODEL_CONNECTOR_PROPERTY,
          viewDescriptor.getModelDescriptor());

      IEntityFactory entityFactory = mockBackController.getEntityFactory();

      modelConnector.setConnectorValue(entityFactory
          .createEntityInstance(((IComponentDescriptor<IEntity>) viewDescriptor
              .getModelDescriptor()).getComponentContract()));

      mockFrontController.getMvcBinder().bind(view.getConnector(),
          modelConnector);
    }

    JFrame testFrame = new JFrame("View tester");
    testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    testFrame.getContentPane().setLayout(new BorderLayout());
    testFrame.getContentPane().add(view.getPeer(), BorderLayout.CENTER);

    testFrame.pack();
    testFrame.setSize(450, 300);
    System.setProperty("sun.awt.exception.handler",
        TesterExceptionHandler.class.getName());
    SwingUtil.centerOnScreen(testFrame);
    testFrame.setVisible(true);
  }

  /**
   * Sets the beanFactorySelector.
   *
   * @param beanFactorySelector
   *          the beanFactorySelector to set.
   */
  public void setBeanFactorySelector(String beanFactorySelector) {
    this.beanFactorySelector = beanFactorySelector;
  }

  /**
   * Sets the applicationContextKey.
   *
   * @param applicationContextKey
   *          the applicationContextKey to set.
   */
  public void setApplicationContextKey(String applicationContextKey) {
    this.applicationContextKey = applicationContextKey;
  }

  /**
   * Sets the language.
   *
   * @param language
   *          the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Sets the viewId.
   *
   * @param viewId
   *          the viewId to set.
   */
  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  private ApplicationContext getApplicationContext() {
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator
        .getInstance(beanFactorySelector);
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    return (ApplicationContext) bf.getFactory();
  }

  /**
   * Specialized exception handler for the tester event dispatch thread.
   *
     * @author Vincent Vandenschrick
   */
  public static class TesterExceptionHandler {

    /**
     * Handles a uncaught exception.
     *
     * @param t
     *          the uncaught exception.
     */
    public void handle(Throwable t) {
      LOG.error("An unexpected error occurred.", t);
      JOptionPane.showMessageDialog(null, t.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
