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
package org.jspresso.framework.security.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

/**
 * Simply leverage {@code http.proxyUser} and {@code http.proxyPassword} to install a custom proxy authenticator in its
 * constructor. See RFE #1218
 *
 * @author Vincent Vandenschrick
 */
public class ProxyAuthenticatorInit {

  /**
   * {@code http.proxyUser}.
   */
  public static final String HTTP_PROXY_USER     = "http.proxyUser";

  /**
   * {@code http.proxyPassword}.
   */
  public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";

  /**
   * Instantiates a new Proxy authenticator init.
   */
  public ProxyAuthenticatorInit() {
    init();
  }

  /**
   * Installs the custom proxy authenticator if necessary.
   */
  protected void init() {
    final String proxyUser = System.getProperty(HTTP_PROXY_USER);
    final String proxyPassword = System.getProperty(HTTP_PROXY_PASSWORD, "");
    if (proxyUser != null) {
      Authenticator.setDefault(new Authenticator() {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
        }
      });
    }
  }

  private int connectAndDump(String url) throws IOException {
/*
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(System.getProperty("http.proxyHost"),
        Integer.parseInt(System.getProperty("http.proxyPort"))));
*/
    URLConnection connection = new URL(url).openConnection(/*proxy*/);
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
    }
    return ((HttpURLConnection) connection).getResponseCode();
  }

  /**
   * The entry point of application.
   *
   * @param args
   *     the input arguments
   * @throws IOException
   *     the io exception
   */
  public static void main(String... args) throws IOException {
    if (args.length == 0) {
      System.out.println("Usage : java -Dhttp.proxyHost=proxy.example.com" +
          ".net -Dhttp.proxyPort=8080 -Dhttp.auth.ntlm.domain=NTML_DOMAIN -Dhttp" +
          ".proxyUser=PROXY_USER -Dhttp.proxyPassword=PROXY_PASS org.jspresso.framework.security.auth" +
          ".ProxyAuthenticatorInit URL");
      return;
    }
    ProxyAuthenticatorInit instance = new ProxyAuthenticatorInit();
    int responseCode = instance.connectAndDump(args[0]);
    System.out.println("Response code is : " + responseCode);
  }
}
