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
package org.jspresso.framework.application.launch.ulc.development;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jspresso.framework.application.launch.ulc.ClassInvoker;
import org.jspresso.framework.application.launch.ulc.ExtendedFileService;
import org.jspresso.framework.application.launch.ulc.FileExists;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.util.swing.splash.SplashWindow;
import org.jspresso.framework.util.url.UrlHelper;

import com.ulcjava.base.client.ClientEnvironmentAdapter;
import com.ulcjava.base.client.IMessageService;
import com.ulcjava.base.development.DevelopmentRunner;

/**
 * Custom jnlp runner to cope with formatted textfield font bug.
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
