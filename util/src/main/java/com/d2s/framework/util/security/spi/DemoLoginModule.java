/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.security.spi;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;


/**
 * login : demo. password : demo.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DemoLoginModule implements LoginModule {

  // initial state
  private Subject         subject;
  private CallbackHandler callbackHandler;
  @SuppressWarnings("unused")
  private Map             sharedState;
  private Map             options;

  // configurable option
  private boolean         debug           = false;

  // the authentication status
  private boolean         succeeded       = false;
  private boolean         commitSucceeded = false;

  // username and password
  private String          username;
  private char[]          password;

  // testUser's DemoPrincipal
  private DemoPrincipal   userPrincipal;

  /**
   * Initialize this <code>LoginModule</code>.
   * <p>
   * 
   * @param aSubject
   *          the <code>Subject</code> to be authenticated.
   *          <p>
   * @param aCallbackHandler
   *          a <code>CallbackHandler</code> for communicating with the end
   *          user (prompting for user names and passwords, for example).
   *          <p>
   * @param aSharedState
   *          shared <code>LoginModule</code> state.
   *          <p>
   * @param aOptions
   *          options specified in the login <code>Configuration</code> for
   *          this particular <code>LoginModule</code>.
   */
  public void initialize(Subject aSubject, CallbackHandler aCallbackHandler,
      Map aSharedState, Map aOptions) {

    subject = aSubject;
    callbackHandler = aCallbackHandler;
    sharedState = aSharedState;
    options = aOptions;

    // initialize any configured options
    debug = "true".equalsIgnoreCase((String) options.get("debug"));
  }

  /**
   * Authenticate the user by prompting for a user name and password.
   * <p>
   * 
   * @return true in all cases since this <code>LoginModule</code> should not
   *         be ignored.
   * @exception LoginException
   *              if the authentication fails.
   *              <p>
   * @exception LoginException
   *              if this <code>LoginModule</code> is unable to perform the
   *              authentication.
   */
  public boolean login() throws LoginException {

    // prompt for a user name and password
    if (callbackHandler == null) {
      throw new LoginException("Error: no CallbackHandler available "
          + "to garner authentication information from the user");
    }

    Callback[] callbacks = new Callback[2];
    callbacks[0] = new NameCallback("user name : ");
    callbacks[1] = new PasswordCallback("password  : ", false);

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

    // print debugging information
    if (debug) {
      System.out.println("\t\t[SampleLoginModule] "
          + "user entered user name: " + username);
      System.out.print("\t\t[SampleLoginModule] " + "user entered password: ");
      for (int i = 0; i < password.length; i++) {
        System.out.print(password[i]);
      }
      System.out.println();
    }

    // verify the username/password
    boolean usernameCorrect = false;
    if (username.equals("demo")) {
      usernameCorrect = true;
    }
    if (usernameCorrect && password.length == 4 && password[0] == 'd'
        && password[1] == 'e' && password[2] == 'm' && password[3] == 'o') {

      if (debug) {
        System.out.println("\t\t[SampleLoginModule] "
            + "authentication succeeded");
      }
      succeeded = true;
      return true;
    }
    // authentication failed -- clean out state
    if (debug) {
      System.out.println("\t\t[SampleLoginModule] " + "authentication failed");
    }
    succeeded = false;
    username = null;
    for (int i = 0; i < password.length; i++) {
      password[i] = ' ';
    }
    password = null;
    if (!usernameCorrect) {
      throw new FailedLoginException("User Name Incorrect");
    }
    throw new FailedLoginException("Password Incorrect");
  }

  /**
   * <p>
   * This method is called if the LoginContext's overall authentication
   * succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
   * LoginModules succeeded).
   * <p>
   * If this LoginModule's own authentication attempt succeeded (checked by
   * retrieving the private state saved by the <code>login</code> method),
   * then this method associates a <code>DemoPrincipal</code> with the
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
    userPrincipal = new DemoPrincipal(username);
    if (!subject.getPrincipals().contains(userPrincipal)) {
      subject.getPrincipals().add(userPrincipal);
    }

    if (debug) {
      System.out.println("\t\t[SampleLoginModule] "
          + "added DemoPrincipal to Subject");
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
