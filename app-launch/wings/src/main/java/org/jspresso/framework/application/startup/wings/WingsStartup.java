/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.startup.wings;

import java.util.Locale;

import javax.swing.Action;

import org.jspresso.framework.application.startup.AbstractFrontendStartup;
import org.wings.SComponent;
import org.wings.SIcon;
import org.wings.session.SessionManager;

/**
 * Default wings startup class.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class WingsStartup extends
    AbstractFrontendStartup<SComponent, SIcon, Action> {

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
