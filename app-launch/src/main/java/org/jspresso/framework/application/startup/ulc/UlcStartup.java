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
package org.jspresso.framework.application.startup.ulc;

import java.util.Locale;

import org.jspresso.framework.application.startup.AbstractStartup;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.IApplication;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * Default ULC startup class.
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
