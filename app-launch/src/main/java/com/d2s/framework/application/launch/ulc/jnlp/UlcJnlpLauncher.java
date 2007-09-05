/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.launch.ulc.jnlp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import com.d2s.framework.application.launch.ulc.ClassInvoker;
import com.d2s.framework.application.launch.ulc.ExtendedFileService;
import com.d2s.framework.application.launch.ulc.FileExists;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.util.swing.splash.SplashWindow;
import com.d2s.framework.util.url.UrlHelper;
import com.ulcjava.base.client.ClientEnvironmentAdapter;
import com.ulcjava.base.client.ConnectorException;
import com.ulcjava.base.client.IMessageService;
import com.ulcjava.base.client.ISessionStateListener;
import com.ulcjava.base.client.UISession;
import com.ulcjava.base.client.launcher.DefaultSessionStateListener;
import com.ulcjava.base.shared.internal.IllegalArgumentException;
import com.ulcjava.base.shared.logging.Level;
import com.ulcjava.base.shared.logging.LogManager;
import com.ulcjava.base.shared.logging.SimpleLogManager;
import com.ulcjava.container.servlet.client.CookieRequestPropertyStore;
import com.ulcjava.container.servlet.client.ServletConnector;
import com.ulcjava.environment.jnlp.client.AbstractJnlpLauncher;

/**
 * Custom jnlp runner to cope with formatted textfield font bug.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class UlcJnlpLauncher extends AbstractJnlpLauncher {

  private static final String   USAGE_TEXT = "JNLP file paramters: \n"
                                               + "\t<urlString> the ULC application URL string\n"
                                               + "\t<keepAliveInterval> the keep alive interval\n"
                                               + "\t[ <logLevel> ] the log level (optional)\n"
                                               + "\t{ -<key> <value> } the user parameters (optional, multiple allowed)\n";
  private static int getKeepAliveInterval(String[] args) {
    try {
      return Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Parameter <keepAliveInterval> must be an integer.");
    }
  }
  private static String getLogLevel(String[] args) {
    if (args.length < 3) {
      return null;
    }

    String result = args[2];
    if (result.startsWith("-")) {
      return null;
    }
    return result;
  }
  private static String getUrlString(String[] args) {
    return args[0];
  }
  private static Properties getUserParameters(String[] args) {
    Properties result = new Properties();
    for (Iterator i = Arrays.asList(args).iterator(); i.hasNext();) {
      String key = (String) i.next();
      if (key.startsWith("-")) {
        key = key.substring(1);
        if (!i.hasNext()) {
          throw new IllegalArgumentException(
              "User parameters must have format -<key> <value>.");
        }

        result.put(key, i.next());
      }
    }

    return result;
  }
  /**
   * Overriden to cope with formatted textfield font bug.
   * 
   * @param args
   *          arguments.
   * @throws MalformedURLException
   *           whenever the startup url is malformed.
   */
  public static void main(String[] args) throws MalformedURLException {
    String splashUrl = null;
    List<String> filteredArgsBuffer = new ArrayList<String>();
    for (int i = 0; i < args.length; i++) {
      if ("-splash".equals(args[i])) {
        splashUrl = args[i + 1];
        i++;
      } else {
        filteredArgsBuffer.add(args[i]);
      }
    }
    String[] filteredArgs = filteredArgsBuffer
        .toArray(new String[filteredArgsBuffer.size()]);
    if (filteredArgs.length < 2) {
      throw new IllegalArgumentException(
          "JNLP file parameters <urlString> and <keepAliveInterval> are mandatory.\n\n"
              + USAGE_TEXT);
    }

    Properties props = ClientEnvironmentAdapter.getClientInfo()
        .getSystemProperties();
    props.setProperty("java.io.tmpdir", System.getProperty("java.io.tmpdir"));
    SwingUtil.installDefaults();

    String logLevel = getLogLevel(filteredArgs);
    if (logLevel != null) {
      if (LogManager.getLogManager() instanceof SimpleLogManager) {
        SimpleLogManager simpleLogManager = (SimpleLogManager) LogManager
            .getLogManager();
        simpleLogManager.setLevel(Level.parse(logLevel));
      }
    }
    UlcJnlpLauncher launcher = new UlcJnlpLauncher(new URL(
        getUrlString(filteredArgs)), getKeepAliveInterval(filteredArgs),
        getUserParameters(filteredArgs), splashUrl, Locale.getDefault());
    launcher.start();
  }
  private ResourceBundle        bundle;

  private int                   keepAliveInterval;

  private List<IMessageService> messageHandlers;

  private String                splashUrl;

  private URL                   url;

  private Properties            userParameters;

  private UlcJnlpLauncher(URL url, int keepAliveInterval,
      Properties userParameters, String splashUrl, Locale locale) {
    this.url = url;
    this.keepAliveInterval = keepAliveInterval;
    this.userParameters = userParameters;
    this.splashUrl = splashUrl;
    bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    registerMessageHandler(new ClassInvoker());
    registerMessageHandler(new FileExists());
    if (splashUrl != null) {
      registerMessageHandler(new IMessageService() {

        public void handleMessage(String msg) {
          if ("appStarted".equals(msg)) {
            SplashWindow.disposeSplash();
          }
        }
      });
    }

    ClientEnvironmentAdapter.setMessageService(new IMessageService() {

      public void handleMessage(String msg) {
        if (messageHandlers != null) {
          for (IMessageService messageHandler : messageHandlers) {
            messageHandler.handleMessage(msg);
          }
        }
      }
    });
    ClientEnvironmentAdapter.setFileService(new ExtendedFileService());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ISessionStateListener createSessionStateListener() {
    return new DefaultSessionStateListener() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void sessionError(UISession session, Throwable reason) {
        if (reason instanceof ConnectorException) {
          showMessageDialog(bundle.getString("error"), bundle
              .getString("error.connection"), bundle
              .getString("error.connection.description"));
          start();
        } else {
          super.sessionError(session, reason);
        }
      }
    };
  }

  /**
   * Registers a new message handler to which client messages will be delivered.
   * 
   * @param messageHandler
   *          the new message handler to be delivered.
   */
  private void registerMessageHandler(IMessageService messageHandler) {
    if (messageHandlers == null) {
      messageHandlers = new ArrayList<IMessageService>();
    }
    messageHandlers.add(messageHandler);
  }

  private UISession start() {
    if (splashUrl != null) {
      SplashWindow.splash(UrlHelper.createURL(splashUrl));
    }
    return start(new ServletConnector(new CookieRequestPropertyStore(url), url,
        keepAliveInterval), userParameters);
  }
}
