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
package org.jspresso.framework.application.frontend.command.remote;

import java.util.List;

/**
 * The interface for remote command handlers.
 *
 * @author Vincent Vandenschrick
 */
public interface IRemoteCommandHandler {

  /**
   * Receives and handle a list of commands.
   *
   * @param commands
   *          the command list.
   * @return the resulting commands.
   */
  List<RemoteCommand> handleCommands(List<RemoteCommand> commands);

  /**
   * Registers a command for next round-trip.
   *
   * @param command
   *          the command to register.
   */
  void registerCommand(RemoteCommand command);
}
