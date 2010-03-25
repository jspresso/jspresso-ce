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
package org.jspresso.framework.security;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

/**
 * A simplistic JAAS callback handler that auto handles user and password
 * callback.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UsernamePasswordHandler implements CallbackHandler {

  private String password;
  private String username;

  /**
   * Resets all data.
   */
  public void clear() {
    setUsername(null);
    setPassword(null);
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
  public void handle(Callback[] callbacks) {
    for (int i = 0; i < callbacks.length; i++) {
      if (callbacks[i] instanceof NameCallback) {
        ((NameCallback) callbacks[i]).setName(getUsername());
      } else if (callbacks[i] instanceof PasswordCallback) {
        if (getPassword() != null) {
          ((PasswordCallback) callbacks[i]).setPassword(getPassword()
              .toCharArray());
        } else {
          ((PasswordCallback) callbacks[i]).setPassword(null);
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
}
