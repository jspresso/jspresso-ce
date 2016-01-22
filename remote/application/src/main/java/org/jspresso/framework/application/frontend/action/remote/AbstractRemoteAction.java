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
package org.jspresso.framework.application.frontend.action.remote;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;

/**
 * This class serves as base class for remote actions. It provides accessors on
 * commonly used artifacts.
 *
 * @author Vincent Vandenschrick
 * @internal
 */
public abstract class AbstractRemoteAction extends
    FrontendAction<RComponent, RIcon, RAction> {

  /**
   * Registers a command for remote execution.
   *
   * @param command
   *          the command to register.
   * @param context
   *          the action context.
   */
  protected void registerCommand(RemoteCommand command,
      Map<String, Object> context) {
    ((IRemoteCommandHandler) getController(context)).registerCommand(command);
  }
}
