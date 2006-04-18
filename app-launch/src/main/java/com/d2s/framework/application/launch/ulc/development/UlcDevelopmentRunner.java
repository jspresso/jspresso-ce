/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.launch.ulc.development;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.util.swing.splash.SplashWindow;
import com.d2s.framework.util.url.UrlHelper;
import com.ulcjava.base.client.ClientEnvironmentAdapter;
import com.ulcjava.base.client.IMessageService;
import com.ulcjava.base.development.DevelopmentRunner;

/**
 * Custom jnlp runner to cope with formatted textfield font bug.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class UlcDevelopmentRunner {

  private UlcDevelopmentRunner() {
    // Helper class constructor.
  }

  /**
   * Overriden to cope with formatted textfield font bug.
   * 
   * @param args
   *          arguments.
   */
  public static void main(String[] args) {
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
      SplashWindow.splash(UrlHelper.createURL(splashUrl, ClassLoader
          .getSystemClassLoader()));
      ClientEnvironmentAdapter.setMessageService(new IMessageService() {

        public void handleMessage(String msg) {
          if ("appStarted".equals(msg)) {
            SplashWindow.disposeSplash();
          }
        }
      });
    }
    SwingUtil.installDefaults();
    DevelopmentRunner.main(filteredArgs.toArray(new String[0]));
  }
}
