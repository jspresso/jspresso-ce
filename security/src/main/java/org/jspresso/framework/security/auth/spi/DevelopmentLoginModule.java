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
package org.jspresso.framework.security.auth.spi;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.security.LoginUtils;

/**
 * A development login module with configuration parametrized user, password,
 * roles and custom properties.
 *
 * @author Vincent Vandenschrick
 */
public class DevelopmentLoginModule implements LoginModule {

  private static final String CUSTOM_PROPERTY_OPT = "custom";
  private static final String PASSWORD_OPT        = "password";
  private static final String ROLES_OPT           = "roles";
  private static final String USER_OPT            = "user";

  private static final String SHARED_NAME_KEY     = "javax.security.auth.login.name";
  private static final String SHARED_PASSWORD_KEY = "javax.security.auth.login.password";

  private CallbackHandler     callbackHandler;
  private boolean             commitSucceeded     = false;
  private Map<String, ?>      options;
  private Map<String, ?>      sharedState;
  private char[]              password;
  private Subject             subject;
  private boolean             succeeded           = false;
  private String              suffix;
  private String              username;
  private UserPrincipal       userPrincipal;

  /**
   * <p>
   * This method is called if the LoginContext's overall authentication failed.
   * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did
   * not succeed).
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the {@code login} and
   * {@code commit} methods), then this method cleans up any state that was
   * originally saved.
   * <p>
   *
   * @return false if this LoginModule's own login and/or commit attempts
   *         failed, and true otherwise.
   */
  @Override
  public boolean abort() {
    if (!succeeded) {
      Callback[] callbacks = new Callback[1];
      callbacks[0] = new TextOutputCallback(TextOutputCallback.ERROR,
          LoginUtils.LOGIN_FAILED);
      try {
        callbackHandler.handle(callbacks);
      } catch (IOException | UnsupportedCallbackException ex) {
        // NO-OP.
      }
      return false;
    }
    if (!commitSucceeded) {
      // login succeeded but overall authentication failed
      succeeded = false;
      username = null;
      suffix = "";
      if (password != null) {
        for (int i = 0; i < password.length; i++) {
          password[i] = ' ';
        }
        password = null;
      }
      userPrincipal = null;
    } else {
      // overall authentication succeeded and commit succeeded,
      // but someone else's commit failed
      logout();
    }
    return true;
  }

  /**
   * <p>
   * This method is called if the LoginContext's overall authentication
   * succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
   * LoginModules succeeded).
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the {@code login} method), then
   * this method associates a {@code UserPrincipal} with the
   * {@code Subject} located in the {@code LoginModule}. If this
   * LoginModule's own authentication attempted failed, then this method removes
   * any state that was originally saved.
   * <p>
   *
   * @return true if this LoginModule's own login and commit attempts succeeded,
   *         or false otherwise.
   */
  @Override
  public boolean commit() {
    if (!succeeded) {
      return false;
    }
    // assume the user we authenticated is the DemoPrincipal
    userPrincipal = new UserPrincipal(username);
    for (Map.Entry<String, ?> option : options.entrySet()) {
      if (option.getKey().startsWith(CUSTOM_PROPERTY_OPT + suffix)) {
        userPrincipal
            .putCustomProperty(
                option.getKey().substring(
                    (CUSTOM_PROPERTY_OPT + suffix).length() + 1),
                option.getValue());
      }
    }
    if (!subject.getPrincipals().contains(userPrincipal)) {
      subject.getPrincipals().add(userPrincipal);
    }

    String roles = (String) options.get(ROLES_OPT + suffix);
    if (roles != null) {
      Group rolesGroup = new SimpleGroup(SecurityHelper.ROLES_GROUP_NAME);
      String[] rolesArray = roles.split(",");
      for (String role : rolesArray) {
        rolesGroup.addMember(new SimplePrincipal(role));
      }
      subject.getPrincipals().add(rolesGroup);
    }

    // in any case, clean out state
    username = null;
    suffix = "";
    for (int i = 0; i < password.length; i++) {
      password[i] = ' ';
    }
    password = null;

    commitSucceeded = true;
    return true;
  }

  /**
   * Initialize this {@code LoginModule}.
   * <p>
   *
   * @param aSubject
   *          the {@code Subject} to be authenticated.
   *          <p>
   * @param aCallbackHandler
   *          a {@code CallbackHandler} for communicating with the end user
   *          (prompting for user names and passwords, for example).
   *          <p>
   * @param aSharedState
   *          shared {@code LoginModule} state.
   *          <p>
   * @param aOptions
   *          options specified in the login {@code Configuration} for this
   *          particular {@code LoginModule}.
   */
  @Override
  public void initialize(Subject aSubject, CallbackHandler aCallbackHandler,
      Map<String, ?> aSharedState, Map<String, ?> aOptions) {

    subject = aSubject;
    callbackHandler = aCallbackHandler;
    sharedState = aSharedState;
    options = aOptions;
  }

  /**
   * Authenticate the user by prompting for a user name and password.
   * <p>
   *
   * @return true in all cases since this {@code LoginModule} should not be
   *         ignored.
   * @exception LoginException
   *              if this {@code LoginModule} is unable to perform the
   *              authentication.
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean login() throws LoginException {

    // prompt for a user name and password
    if (callbackHandler == null) {
      throw new IllegalStateException("Error: no CallbackHandler available "
          + "to garner authentication information from the user");
    }

    Callback[] callbacks = new Callback[3];
    callbacks[0] = new NameCallback(LoginUtils.USER);
    callbacks[1] = new PasswordCallback(LoginUtils.PASSWORD, false);
    callbacks[2] = new TextOutputCallback(TextOutputCallback.INFORMATION,
        LoginUtils.CRED_MESSAGE);

    try {
      if (sharedState != null && sharedState.containsKey(SHARED_NAME_KEY)
          && sharedState.containsKey(SHARED_PASSWORD_KEY)) {
        Object sharedName = sharedState.get(SHARED_NAME_KEY);
        if (sharedName instanceof Principal) {
          username = ((Principal) sharedState.get(SHARED_NAME_KEY)).getName();
        } else if (sharedState.get(SHARED_NAME_KEY) != null) {
          username = sharedState.get(SHARED_NAME_KEY).toString();
        }
        Object sharedPassword = sharedState.get(SHARED_PASSWORD_KEY);
        if (sharedPassword instanceof char[]) {
          password = (char[]) sharedState.get(SHARED_PASSWORD_KEY);
        } else if (sharedState.get(SHARED_PASSWORD_KEY) != null) {
          password = sharedState.get(SHARED_PASSWORD_KEY).toString()
              .toCharArray();
        }
      } else {
        callbackHandler.handle(callbacks);
        username = ((NameCallback) callbacks[0]).getName();
        char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
        if (tmpPassword != null) {
          password = new char[tmpPassword.length];
          System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
          ((PasswordCallback) callbacks[1]).clearPassword();
        } else {
          password = null;
        }
      }
    } catch (java.io.IOException ioe) {
      throw new RuntimeException(ioe);
    } catch (UnsupportedCallbackException uce) {
      throw new RuntimeException("Error: " + uce.getCallback().toString()
          + " not available to garner authentication information "
          + "from the user");
    }

    // verify the username/password
    boolean usernameCorrect = checkUserName();
    String modulePassword = (String) options.get(PASSWORD_OPT + suffix);
    if (usernameCorrect) {
      if (modulePassword == null || password.length == 0) {
        succeeded = true;
      } else {
        if (modulePassword.length() == password.length) {
          succeeded = true;
          for (int i = 0; succeeded && i < modulePassword.length(); i++) {
            if (modulePassword.charAt(i) != password[i]) {
              succeeded = false;
            }
          }
        }
      }
      // Populate shared state
      ((Map<String, Object>) sharedState).put(SHARED_NAME_KEY, username);
      ((Map<String, Object>) sharedState).put(SHARED_PASSWORD_KEY, password);
      return true;
    }
    succeeded = false;
    username = null;
    suffix = "";
    if (password != null) {
      for (int i = 0; i < password.length; i++) {
        password[i] = ' ';
      }
      password = null;
    }
    throw new FailedLoginException(LoginUtils.PASSWORD_FAILED);
  }

  /**
   * Logout the user.
   * <p>
   * This method removes the {@code DemoPrincipal} that was added by the
   * {@code commit} method.
   * <p>
   *
   * @return true in all cases since this {@code LoginModule} should not be
   *         ignored.
   */
  @Override
  public boolean logout() {
    subject.getPrincipals().remove(userPrincipal);
    succeeded = false;
    succeeded = commitSucceeded;
    username = null;
    suffix = "";
    if (password != null) {
      for (int i = 0; i < password.length; i++) {
        password[i] = ' ';
      }
      password = null;
    }
    userPrincipal = null;
    return true;
  }

  private boolean checkUserName() {
    boolean usernameCorrect = false;
    boolean atLeastOneUserEntryFound = false;
    for (Map.Entry<String, ?> optionEntry : options.entrySet()) {
      if (optionEntry.getKey().startsWith(USER_OPT)) {
        atLeastOneUserEntryFound = true;
        if (optionEntry.getValue().toString().equalsIgnoreCase(username)) {
          usernameCorrect = true;
          suffix = optionEntry.getKey().substring(USER_OPT.length());
        }
      }
    }
    if (atLeastOneUserEntryFound) {
      return usernameCorrect;
    }
    return true;
  }
}
