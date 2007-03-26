/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.wings;

import java.util.Locale;

import org.wings.session.SessionManager;

import com.d2s.framework.application.frontend.startup.AbstractStartup;

/**
 * Default wings startup class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class WingsStartup extends AbstractStartup {

  /**
   * Constructs a new <code>WingsStartup</code> instance.
   */
  public WingsStartup() {
    start();
  }

  /**
   * Returns the client context default locale.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Locale getStartupLocale() {
    return SessionManager.getSession().getLocale();
  }

}
