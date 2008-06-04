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
package org.jspresso.framework.security.auth.spi;

import java.io.IOException;
import java.security.acl.Group;
import java.util.Map;
import java.util.ResourceBundle;

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
public class DevelopmentLoginModule implements LoginModule {

  private static final String CUSTOM_PROPERTY_OPT = "custom.";
  private static final String PASSWORD_OPT        = "password";
  private static final String ROLES_OPT           = "roles";
  private static final String USER_OPT            = "user";

  private ResourceBundle      bundle              = ResourceBundle
                                                      .getBundle("org.jspresso.framework.util.security.LoginUtils$DlmBundle");
  private CallbackHandler     callbackHandler;
  private boolean             commitSucceeded     = false;
  private Map<String, ?>      options;
  private char[]              password;
  private Subject             subject;
  private boolean             succeeded           = false;
  private String              username;
  private UserPrincipal       userPrincipal;

  /**
   * <p>
   * This method is called if the LoginContext's overall authentication failed.
   * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did
   * not succeed).
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the <code>login</code> and
   * <code>commit</code> methods), then this method cleans up any state that
   * was originally saved.
   * <p>
   * 
   * @return false if this LoginModule's own login and/or commit attempts
   *         failed, and true otherwise.
   */
  public boolean abort() {
    if (!succeeded) {
      Callback[] callbacks = new Callback[1];
      callbacks[0] = new TextOutputCallback(TextOutputCallback.ERROR, bundle
          .getString(LoginUtils.LOGIN_FAILED));
      try {
        callbackHandler.handle(callbacks);
      } catch (IOException ex) {
        // NO-OP.
      } catch (UnsupportedCallbackException ex) {
        // NO-OP.
      }
      return false;
    } else if (succeeded && !commitSucceeded) {
      // login succeeded but overall authentication failed
      succeeded = false;
      username = null;
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
   * retrieving the private state saved by the <code>login</code> method),
   * then this method associates a <code>UserPrincipal</code> with the
   * <code>Subject</code> located in the <code>LoginModule</code>. If this
   * LoginModule's own authentication attempted failed, then this method removes
   * any state that was originally saved.
   * <p>
   * 
   * @return true if this LoginModule's own login and commit attempts succeeded,
   *         or false otherwise.
   */
  public boolean commit() {
    if (!succeeded) {
      return false;
    }
    // assume the user we authenticated is the DemoPrincipal
    userPrincipal = new UserPrincipal(username);
    for (Map.Entry<String, ?> option : options.entrySet()) {
      if (option.getKey().startsWith(CUSTOM_PROPERTY_OPT)) {
        userPrincipal.putCustomProperty(option.getKey().substring(
            CUSTOM_PROPERTY_OPT.length()), option.getValue());
      }
    }
    if (!subject.getPrincipals().contains(userPrincipal)) {
      subject.getPrincipals().add(userPrincipal);
    }

    String roles = (String) options.get(ROLES_OPT);
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
    for (int i = 0; i < password.length; i++) {
      password[i] = ' ';
    }
    password = null;

    commitSucceeded = true;
    return true;
  }

  /**
   * Initialize this <code>LoginModule</code>.
   * <p>
   * 
   * @param aSubject
   *            the <code>Subject</code> to be authenticated.
   *            <p>
   * @param aCallbackHandler
   *            a <code>CallbackHandler</code> for communicating with the end
   *            user (prompting for user names and passwords, for example).
   *            <p>
   * @param aSharedState
   *            shared <code>LoginModule</code> state.
   *            <p>
   * @param aOptions
   *            options specified in the login <code>Configuration</code> for
   *            this particular <code>LoginModule</code>.
   */
  public void initialize(Subject aSubject, CallbackHandler aCallbackHandler,
      Map<String, ?> aSharedState, Map<String, ?> aOptions) {

    subject = aSubject;
    callbackHandler = aCallbackHandler;
    options = aOptions;
  }

  /**
   * Authenticate the user by prompting for a user name and password.
   * <p>
   * 
   * @return true in all cases since this <code>LoginModule</code> should not
   *         be ignored.
   * @exception LoginException
   *                if the authentication fails.
   *                <p>
   * @exception LoginException
   *                if this <code>LoginModule</code> is unable to perform the
   *                authentication.
   */
  public boolean login() throws LoginException {

    // prompt for a user name and password
    if (callbackHandler == null) {
      throw new LoginException("Error: no CallbackHandler available "
          + "to garner authentication information from the user");
    }

    Callback[] callbacks = new Callback[3];
    callbacks[0] = new NameCallback(bundle.getString(LoginUtils.USER));
    callbacks[1] = new PasswordCallback(bundle.getString(LoginUtils.PASSWORD),
        false);
    callbacks[2] = new TextOutputCallback(TextOutputCallback.INFORMATION,
        bundle.getString(LoginUtils.CRED_MESSAGE));

    try {
      callbackHandler.handle(callbacks);
      username = ((NameCallback) callbacks[0]).getName();
      char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();
      if (tmpPassword == null) {
        // treat a NULL password as an empty password
        tmpPassword = new char[0];
      }
      password = new char[tmpPassword.length];
      System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
      ((PasswordCallback) callbacks[1]).clearPassword();

    } catch (java.io.IOException ioe) {
      throw new LoginException(ioe.toString());
    } catch (UnsupportedCallbackException uce) {
      throw new LoginException("Error: " + uce.getCallback().toString()
          + " not available to garner authentication information "
          + "from the user");
    }

    // verify the username/password
    boolean usernameCorrect = false;
    String moduleUserName = (String) options.get(USER_OPT);
    if (moduleUserName == null || moduleUserName.equals(username)) {
      usernameCorrect = true;
    }
    String modulePassword = (String) options.get(PASSWORD_OPT);
    if (usernameCorrect) {
      if (modulePassword == null) {
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
      return true;
    }
    succeeded = false;
    username = null;
    for (int i = 0; i < password.length; i++) {
      password[i] = ' ';
    }
    password = null;
    if (!usernameCorrect) {
      throw new FailedLoginException(bundle.getString(LoginUtils.USER_FAILED));
    }
    throw new FailedLoginException(bundle.getString(LoginUtils.PASSWORD_FAILED));
  }

  /**
   * Logout the user.
   * <p>
   * This method removes the <code>DemoPrincipal</code> that was added by the
   * <code>commit</code> method.
   * <p>
   * 
   * @return true in all cases since this <code>LoginModule</code> should not
   *         be ignored.
   */
  public boolean logout() {
    subject.getPrincipals().remove(userPrincipal);
    succeeded = false;
    succeeded = commitSucceeded;
    username = null;
    if (password != null) {
      for (int i = 0; i < password.length; i++) {
        password[i] = ' ';
      }
      password = null;
    }
    userPrincipal = null;
    return true;
  }
}
