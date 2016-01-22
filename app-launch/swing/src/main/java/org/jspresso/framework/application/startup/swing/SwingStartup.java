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
package org.jspresso.framework.application.startup.swing;

import java.util.Locale;
import java.util.TimeZone;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.jspresso.framework.application.startup.AbstractFrontendStartup;

/**
 * Default swing startup class.
 *
 * @author Vincent Vandenschrick
 */
public abstract class SwingStartup extends
    AbstractFrontendStartup<JComponent, Icon, Action> {

  /**
   * Returns the client context default locale.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Locale getStartupLocale() {
    return Locale.getDefault();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected TimeZone getClientTimeZone() {
    return TimeZone.getDefault();
  }
}
