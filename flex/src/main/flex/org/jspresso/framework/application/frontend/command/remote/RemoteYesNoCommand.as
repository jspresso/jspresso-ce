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


package org.jspresso.framework.application.frontend.command.remote {

import org.jspresso.framework.gui.remote.RAction;

[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand")]
public class RemoteYesNoCommand extends RemoteMessageCommand {

  private var _noAction:RAction;
  private var _yesAction:RAction;

  public function RemoteYesNoCommand() {
    //default constructor.
  }

  public function set noAction(value:RAction):void {
    _noAction = value;
  }

  public function get noAction():RAction {
    return _noAction;
  }

  public function set yesAction(value:RAction):void {
    _yesAction = value;
  }

  public function get yesAction():RAction {
    return _yesAction;
  }
}
}
