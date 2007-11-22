/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.tools.viewtester;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

import com.d2s.framework.binding.IMvcBinder;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.util.collection.ObjectEqualityMap;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Generates D2S powered component java code based on its descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ViewTester {

  private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
  private static final String VIEW_ID                 = "viewId";
  private static final String LANGUAGE                = "language";

  private String              applicationContextKey;
  private String              viewId;
  private String              language;

  /**
   * Starts Code generation for an component.
   * 
   * @param args
   *            the command line arguments.
   */
  @SuppressWarnings("static-access")
  public static void main(String[] args) {
    Options options = new Options();
    options
        .addOption(OptionBuilder
            .withArgName(APPLICATION_CONTEXT_KEY)
            .isRequired()
            .hasArg()
            .withDescription(
                "use given applicationContextKey as registered in the spring BeanFactoryLocator.")
            .create(APPLICATION_CONTEXT_KEY));
    options.addOption(OptionBuilder.withArgName(VIEW_ID).isRequired().hasArg()
        .withDescription(
            "use given view identifier to instanciate and display the view.")
        .create(VIEW_ID));
    options.addOption(OptionBuilder.withArgName(LANGUAGE).hasArg()
        .withDescription(
            "use given locale to instanciate and display the view.").create(
            LANGUAGE));
    CommandLineParser parser = new BasicParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException ex) {
      System.err.println(ex.getLocalizedMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(ViewTester.class.getSimpleName(), options);
      return;
    }

    ViewTester tester = new ViewTester();
    tester
        .setApplicationContextKey(cmd.getOptionValue(APPLICATION_CONTEXT_KEY));
    tester.setViewId(cmd.getOptionValue(VIEW_ID));
    tester.setLanguage(cmd.getOptionValue(LANGUAGE));
    tester.displayView();
  }

  /**
   * Generates the component java source files.
   */
  @SuppressWarnings("unchecked")
  public void displayView() {
    Locale locale;
    if (language != null) {
      locale = new Locale(language);
    } else {
      locale = Locale.getDefault();
    }

    ApplicationContext appContext = getApplicationContext();
    IViewDescriptor viewDescriptor = (IViewDescriptor) appContext
        .getBean(viewId);
    IViewFactory<JComponent, Icon, Action> viewFactory = (IViewFactory<JComponent, Icon, Action>) appContext
        .getBean("viewFactory");
    
    IView<JComponent> view = viewFactory.createView(viewDescriptor, null,
        locale); 
    
    IModelConnectorFactory mapConnectorFactory = (IModelConnectorFactory) appContext.getBean("mapConnectorFactory");
    IValueConnector modelConnector = mapConnectorFactory.createModelConnector(viewDescriptor.getModelDescriptor());
    modelConnector.setConnectorValue(new ObjectEqualityMap<String, Object>());
    
    IMvcBinder mvcBinder = (IMvcBinder) appContext.getBean("mvcBinder");
    mvcBinder.bind(view.getConnector(), modelConnector);
    
    JFrame testFrame = new JFrame("View tester");
    testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    testFrame.getContentPane().setLayout(new BorderLayout());
    testFrame.getContentPane().add(
        view.getPeer(), BorderLayout.CENTER);

    testFrame.pack();
    testFrame.setVisible(true);
  }

  /**
   * Sets the applicationContextKey.
   * 
   * @param applicationContextKey
   *            the applicationContextKey to set.
   */
  public void setApplicationContextKey(String applicationContextKey) {
    this.applicationContextKey = applicationContextKey;
  }

  /**
   * Sets the viewId.
   * 
   * @param viewId
   *            the viewId to set.
   */
  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  /**
   * Sets the language.
   * 
   * @param language
   *            the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  private ApplicationContext getApplicationContext() {
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    return (ApplicationContext) bf.getFactory();
  }
}
