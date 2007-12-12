/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup.swing;

import java.util.Locale;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;

import com.d2s.framework.application.frontend.startup.AbstractStartup;

/**
 * Default swing startup class.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class SwingStartup extends
    AbstractStartup<JComponent, Icon, Action> {

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
