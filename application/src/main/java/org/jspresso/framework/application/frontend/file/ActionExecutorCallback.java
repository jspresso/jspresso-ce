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
package org.jspresso.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;

/**
 * A callback handler which triggers an arbitrary action.
 *
 * @author Vincent Vandenschrick
 */
public class ActionExecutorCallback extends FileToByteArrayCallback {

  private IAction action;

  /**
   * {@inheritDoc}
   */
  @Override
  public void fileChosen(String name, InputStream in,
      IActionHandler actionHandler, Map<String, Object> context) {
    super.fileChosen(name, in, actionHandler, context);
    actionHandler.execute(action, context);
  }

  /**
   * Sets the action.
   *
   * @param action
   *          the action to set.
   */
  public void setAction(IAction action) {
    this.action = action;
  }
}
