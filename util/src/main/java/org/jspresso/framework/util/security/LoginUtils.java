/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import java.util.ListResourceBundle;

/**
 * Some i18n utils for login.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class LoginUtils {

  /**
   * <code>CRED_MESSAGE</code>.
   */
  public static final String CRED_MESSAGE    = "credentialMessage";
  /**
   * <code>LOGIN_FAILED</code>.
   */
  public static final String LOGIN_FAILED    = "loginFailed";
  /**
   * <code>PASSWORD</code>.
   */
  public static final String PASSWORD        = "password";
  /**
   * <code>PASSWORD_FAILED</code>.
   */
  public static final String PASSWORD_FAILED = "passwordIncorrect";
  /**
   * <code>USER</code>.
   */
  public static final String USER            = "user";
  /**
   * <code>USER_FAILED</code>.
   */
  public static final String USER_FAILED     = "userIncorrect";

  private LoginUtils() {
    // Helper class constructor.
  }

  /**
   * English RB.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class DlmBundle_en extends ListResourceBundle {

    static final Object[][] CONTENTS = {
                                         {USER, "User"},
                                         {PASSWORD, "Password"},
                                         {CRED_MESSAGE,
                                             "Enter login information :"},
                                         {USER_FAILED, "User incorrect"},
                                         {PASSWORD_FAILED, "Password incorrect"},
                                         {LOGIN_FAILED, "Login failed"}};

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object[][] getContents() {
      return CONTENTS;
    }

  }

  /**
   * French RB.
   * <p>
   * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class DlmBundle_fr extends ListResourceBundle {

    static final Object[][] CONTENTS = {
                                         {USER, "Utilisateur"},
                                         {PASSWORD, "Mot de passe"},
                                         {CRED_MESSAGE,
                                             "Veuillez saisir vos informations d'identification :"},
                                         {USER_FAILED, "Utilisateur incorrect"},
                                         {PASSWORD_FAILED,
                                             "Mot de passe incorrect"},
                                         {LOGIN_FAILED,
                                             "Identification incorrecte"}};

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object[][] getContents() {
      return CONTENTS;
    }

  }
}
