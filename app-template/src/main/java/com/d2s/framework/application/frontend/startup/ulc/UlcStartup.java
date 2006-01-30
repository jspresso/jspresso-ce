/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.ulc;

import java.util.Locale;

import com.d2s.framework.application.frontend.startup.AbstractStartup;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IApplication;

/**
 * Default ULC startup class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UlcStartup extends AbstractStartup implements IApplication {

  /**
   * Returns the default "ulc-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "ulc-context";
  }

  /**
   * Returns the client context default locale.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Locale getStartupLocale() {
    return ClientContext.getLocale();
  }

  /**
   * {@inheritDoc}
   */
  public void stop() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void activate() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void passivate() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void handleMessage(@SuppressWarnings("unused")
  String message) {
    // NO-OP
  }

}
