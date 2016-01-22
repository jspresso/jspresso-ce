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
package org.jspresso.framework.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 * A simplistic JAAS callback handler that auto handles user and password
 * callback.
 *
 * @author Vincent Vandenschrick
 */
public class UsernamePasswordHandler implements CallbackHandler {

  private String  password;
  private String  username;
  private String  language;
  private String  timeZoneId;
  private boolean rememberMe;

  /**
   * Resets all data.
   */
  public void clear() {
    setUsername(null);
    setPassword(null);
    setLanguage(null);
    setTimeZoneId(null);
  }

  /**
   * Gets the password.
   *
   * @return the password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Gets the username.
   *
   * @return the username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Handles the JAAS callbacks.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void handle(Callback[] callbacks) {
    for (Callback callback : callbacks) {
      if (callback instanceof NameCallback) {
        ((NameCallback) callback).setName(getUsername());
      } else if (callback instanceof PasswordCallback) {
        if (getPassword() != null) {
          ((PasswordCallback) callback).setPassword(getPassword().toCharArray());
        } else {
          ((PasswordCallback) callback).setPassword(null);
        }
        // } else {
        // throw new UnsupportedCallbackException(callbacks[i]);
      }
    }
  }

  /**
   * Sets the password.
   *
   * @param password
   *          the password to set.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Sets the username.
   *
   * @param username
   *          the username to set.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Sets the rememberMe.
   *
   * @param rememberMe
   *          the rememberMe to set.
   */
  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

  /**
   * Gets the rememberMe.
   *
   * @return the rememberMe.
   */
  public boolean isRememberMe() {
    return rememberMe;
  }

  /**
   * Gets language.
   *
   * @return the language
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Sets language.
   *
   * @param language the language
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Gets time zone.
   *
   * @return the time zone
   */
  public String getTimeZoneId() {
    return timeZoneId;
  }

  /**
   * Sets time zone.
   *
   * @param timeZoneId the time zone
   */
  public void setTimeZoneId(String timeZoneId) {
    this.timeZoneId = timeZoneId;
  }
}
