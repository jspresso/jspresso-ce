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
package org.jspresso.framework.util.security;

/**
 * Some i18n utils for login.
 *
 * @author Vincent Vandenschrick
 */
public final class LoginUtils {

  /**
   * {@code CRED_MESSAGE}.
   */
  public static final String CRED_MESSAGE    = "credentialMessage";
  /**
   * {@code LOGIN_FAILED}.
   */
  public static final String LOGIN_FAILED    = "loginFailed";
  /**
   * {@code PASSWORD}.
   */
  public static final String PASSWORD        = "password";
  /**
   * {@code PASSWORD_FAILED}.
   */
  public static final String PASSWORD_FAILED = "passwordIncorrect";
  /**
   * {@code USER}.
   */
  public static final String USER            = "user";
  /**
   * {@code USER_FAILED}.
   */
  public static final String USER_FAILED     = "userIncorrect";

  private LoginUtils() {
    // Helper class constructor.
  }
}
