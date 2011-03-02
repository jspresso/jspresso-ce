/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.http;

import javax.servlet.http.Cookie;

import org.jspresso.framework.util.preferences.IPreferencesStore;

/**
 * An implementation of preference store relying on browser cookies.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CookiePreferencesStore implements IPreferencesStore {

  private String storePath;

  /**
   * Constructs a new <code>CookiePreferencesStore</code> instance.
   */
  public CookiePreferencesStore() {
    this.storePath = "";
  }

  /**
   * Sets the path of this store.
   * 
   * @param storePath
   *          the preferences store path.
   */
  public void setStorePath(String[] storePath) {
    if (storePath != null && storePath.length > 0) {
      StringBuffer buff = new StringBuffer();
      for (int i = 0; i < storePath.length; i++) {
        buff.append(storePath[i]).append('.');
      }
      this.storePath = buff.toString();
    } else {
      this.storePath = "";
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getPreference(String key) {
    String completeKey = storePath + key;
    if (HttpRequestHolder.getServletRequest() != null) {
      Cookie[] cookies = HttpRequestHolder.getServletRequest().getCookies();
      if (cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
          if (completeKey.equals(cookies[i].getName())) {
            return cookies[i].getValue();
          }
        }
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public void putPreference(String key, String value) {
    String completeKey = storePath + key;
    if (HttpRequestHolder.getServletResponse() != null) {
      Cookie cookie = new Cookie(completeKey, value);
      cookie.setMaxAge(Integer.MAX_VALUE);
      HttpRequestHolder.getServletResponse().addCookie(cookie);
    }
  }

  /**
   * Deletes the cookie storing the preference.
   * <p>
   * {@inheritDoc}
   */
  public void removePreference(String key) {
    if (HttpRequestHolder.getServletResponse() != null) {
      Cookie cookie = new Cookie(key, "");
      cookie.setMaxAge(0);
      HttpRequestHolder.getServletResponse().addCookie(cookie);
    }
  }

}
