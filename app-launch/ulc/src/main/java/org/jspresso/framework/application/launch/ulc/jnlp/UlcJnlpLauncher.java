/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.launch.ulc.jnlp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.jspresso.framework.application.launch.ulc.ClassInvoker;
import org.jspresso.framework.application.launch.ulc.ExtendedFileService;
import org.jspresso.framework.application.launch.ulc.FileExists;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.util.swing.splash.SplashWindow;
import org.jspresso.framework.util.url.UrlHelper;

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
import com.ulcjava.container.servlet.client.AppletRequestPropertyStore;
import com.ulcjava.container.servlet.client.ServletConnector;
import com.ulcjava.environment.jnlp.client.AbstractJnlpLauncher;

/**
 * Custom jnlp runner to cope with formatted textfield font bug.
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
   * Overriden to cope with formatted textfield font bug.
   * 
   * @param args
   *          arguments.
   * @throws MalformedURLException
   *           whenever the startup url is malformed.
   */
  public static void main(String[] args) throws MalformedURLException {
    SwingUtil.installDefaults();
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
    for (Iterator<String> i = Arrays.asList(args).iterator(); i.hasNext();) {
      String key = i.next();
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
          int answer = JOptionPane.showConfirmDialog(null,
              bundle.getString("error.connection.description"),
              bundle.getString("error.connection"),
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
          if (answer == JOptionPane.OK_OPTION) {
            start();
          }
          // showMessageDialog(bundle.getString("error"), bundle
          // .getString("error.connection"), bundle
          // .getString("error.connection.description"));
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
    return start(new ServletConnector(
    // new AppletRequestPropertyStore() {
    //
    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // public void handleHeaderFields(KeyValuePair[] kvps) {
    // CookieHandler handler = CookieHandler.getDefault();
    // if (handler != null) {
    // for (int i = 0; i < kvps.length; i++) {
    // KeyValuePair kvp = kvps[i];
    // Map<String, List<String>> headers = new HashMap<String, List<String>>();
    // List<String> values = new ArrayList<String>();
    // if ("Set-Cookie".equals(kvp.getKey())
    // && kvp.getValue().toUpperCase().indexOf("JSESSIONID") < 0) {
    // values.add(kvp.getValue());
    // headers.put("Cookie", values);
    // try {
    // System.out.println("ADDING COOKIE URL    : "
    // + url.toURI().toString());
    // System.out.println("ADDING COOKIE values : " + values);
    // handler.put(url.toURI(), headers);
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // } catch (URISyntaxException ex) {
    // ex.printStackTrace();
    // }
    // }
    // }
    // }
    // super.handleHeaderFields(kvps);
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // public void handleConnection(URLConnection conn) {
    // super.handleConnection(conn);
    // CookieHandler handler = CookieHandler.getDefault();
    // if (handler != null) {
    // String cookieValue = null;
    // Map<String, List<String>> headers = null;
    // try {
    // headers = handler.get(url.toURI(),
    // new HashMap<String, List<String>>());
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // } catch (URISyntaxException ex) {
    // ex.printStackTrace();
    // }
    // if (headers != null) {
    // List<String> values = headers.get("Cookie");
    // for (Iterator<String> iter = values.iterator(); iter.hasNext();) {
    // String v = iter.next();
    // System.out.println("COOKIE VALUE : " + v);
    // if (v.toUpperCase().indexOf("JSESSIONID") < 0) {
    // if (cookieValue == null) {
    // cookieValue = v;
    // } else {
    // cookieValue = cookieValue + ";" + v;
    // }
    // }
    // }
    // }
    // if (cookieValue != null) {
    // System.out.println("COOKIE VALUE SENT: " + cookieValue);
    // }
    // setRequestProperty(new KeyValuePair("Cookie", cookieValue));
    // // setRequestProperty(new KeyValuePair("Cookie", "UP_KEY=demo§demo"));
    // }
    // }
    // },
        /* new CookieRequestPropertyStore(url), */
        new AppletRequestPropertyStore(), url, keepAliveInterval),
        userParameters);
  }
}
