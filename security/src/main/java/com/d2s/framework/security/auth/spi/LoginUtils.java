/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.security.auth.spi;

import java.util.ListResourceBundle;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class LoginUtils {

  /**
   * <code>USER</code>.
   */
  public static final String USER            = "user";
  /**
   * <code>PASSWORD</code>.
   */
  public static final String PASSWORD        = "password";
  /**
   * <code>CRED_MESSAGE</code>.
   */
  public static final String CRED_MESSAGE    = "credentialMessage";
  /**
   * <code>USER_FAILED</code>.
   */
  public static final String USER_FAILED     = "userIncorrect";
  /**
   * <code>PASSWORD_FAILED</code>.
   */
  public static final String PASSWORD_FAILED = "passwordIncorrect";
  /**
   * <code>LOGIN_FAILED</code>.
   */
  public static final String LOGIN_FAILED    = "loginFailed";

  private LoginUtils() {
    // Helper class constructor.
  }

  /**
   * Default RB.
   * <p>
   * Copyright 2005 Design2See. All rights reserved.
   * <p>
   * 
   * @version $LastChangedRevision$
   * @author Vincent Vandenschrick
   */
  public static class DlmBundle extends ListResourceBundle {

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
   * Copyright 2005 Design2See. All rights reserved.
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
