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
package org.jspresso.framework.application.frontend.action.security;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;

/**
 * This is the frontend action to trigger the application login using the current credential in the frontend controller.
 * If the action is configured with {@code anonymous = true}, then an anonymous login is attempted. If not,
 * a normal login is performed.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class LoginAction<E, F, G> extends FrontendAction<E, F, G> {

  private boolean anonymous = false;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (isAnonymous()) {
      getFrontendController(context).loginAnonymously();
    } else {
      getFrontendController(context).login();
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Is anonymous.
   *
   * @return the boolean
   */
  protected boolean isAnonymous() {
    return anonymous;
  }

  /**
   * Sets anonymous.
   *
   * @param anonymous the anonymous
   */
  public void setAnonymous(boolean anonymous) {
    this.anonymous = anonymous;
  }
}
