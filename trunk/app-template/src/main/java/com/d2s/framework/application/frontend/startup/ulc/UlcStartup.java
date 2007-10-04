/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.ulc;

import java.util.Locale;

import com.d2s.framework.application.frontend.startup.AbstractStartup;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.IApplication;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * Default ULC startup class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class UlcStartup extends
    AbstractStartup<ULCComponent, ULCIcon, IAction> implements IApplication {

  /**
   * {@inheritDoc}
   */
  public void activate() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void handleMessage(@SuppressWarnings("unused")
  String message) {
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
  public void stop() {
    // NO-OP
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
}
