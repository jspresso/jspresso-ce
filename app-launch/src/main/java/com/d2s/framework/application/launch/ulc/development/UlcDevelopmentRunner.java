/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.launch.ulc.development;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.d2s.framework.application.launch.ulc.ClassInvoker;
import com.d2s.framework.application.launch.ulc.ExtendedFileService;
import com.d2s.framework.application.launch.ulc.FileExists;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.util.swing.splash.SplashWindow;
import com.d2s.framework.util.url.UrlHelper;
import com.ulcjava.base.client.ClientEnvironmentAdapter;
import com.ulcjava.base.client.IMessageService;
import com.ulcjava.base.development.DevelopmentRunner;

/**
 * Custom jnlp runner to cope with formatted textfield font bug.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class UlcDevelopmentRunner {

  private static List<IMessageService> messageHandlers;

  private UlcDevelopmentRunner() {
    // Helper class constructor.
  }

  /**
   * Overriden to cope with formatted textfield font bug.
   * 
   * @param args
   *            arguments.
   */
  public static void main(String[] args) {
    SwingUtil.installDefaults();
    ClientEnvironmentAdapter.setMessageService(new IMessageService() {

      public void handleMessage(String msg) {
        if (messageHandlers != null) {
          for (IMessageService messageHandler : messageHandlers) {
            messageHandler.handleMessage(msg);
          }
        }
      }
    });
    registerMessageHandler(new ClassInvoker());
    registerMessageHandler(new FileExists());
    ClientEnvironmentAdapter.setFileService(new ExtendedFileService());

    String splashUrl = null;
    List<String> filteredArgs = new ArrayList<String>();
    for (int i = 0; i < args.length; i++) {
      if ("-splash".equals(args[i])) {
        splashUrl = args[i + 1];
        i++;
      } else {
        filteredArgs.add(args[i]);
      }
    }
    if (splashUrl != null) {
      SplashWindow.splash(UrlHelper.createURL(splashUrl));
      registerMessageHandler(new IMessageService() {

        public void handleMessage(String msg) {
          if ("appStarted".equals(msg)) {
            SplashWindow.disposeSplash();
          }
        }
      });
    }
    Properties props = ClientEnvironmentAdapter.getClientInfo()
        .getSystemProperties();
    props.setProperty("java.io.tmpdir", System.getProperty("java.io.tmpdir"));
    DevelopmentRunner.main(filteredArgs.toArray(new String[0]));
  }

  /**
   * Registers a new message handler to which client messages will be delivered.
   * 
   * @param messageHandler
   *            the new message handler to be delivered.
   */
  public static void registerMessageHandler(IMessageService messageHandler) {
    if (messageHandlers == null) {
      messageHandlers = new ArrayList<IMessageService>();
    }
    messageHandlers.add(messageHandler);
  }
}
