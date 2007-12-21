/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.wings;

import java.util.Locale;

import javax.swing.Action;

import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.session.SessionManager;

import com.d2s.framework.application.frontend.startup.AbstractStartup;

/**
 * Default wings startup class.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class WingsStartup extends
    AbstractStartup<SComponent, SIcon, Action> {

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
