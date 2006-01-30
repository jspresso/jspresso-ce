/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.swing;

import java.util.Locale;

import com.d2s.framework.application.frontend.startup.AbstractStartup;


/**
 * Default swing startup class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SwingStartup extends AbstractStartup {

  /**
   * Returns the default "swing-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "swing-context";
  }

  /**
   * Returns the client context default locale.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Locale getStartupLocale() {
    return Locale.getDefault();
  }

}
