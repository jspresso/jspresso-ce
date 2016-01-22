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
import org.jspresso.framework.gui.remote.RComponent;

[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand")]
public class RemoteInitLoginCommand extends RemoteCommand {

  private var _loginView:RComponent;
  private var _loginActionLists:Array;
  private var _secondaryLoginActionLists:Array;

  public function RemoteInitLoginCommand() {
    //default constructor.
  }

  public function set loginView(value:RComponent):void {
    _loginView = value;
  }

  public function get loginView():RComponent {
    return _loginView;
  }


  public function get loginActionLists():Array {
    return _loginActionLists;
  }

  public function set loginActionLists(value:Array):void {
    _loginActionLists = value;
  }

  public function get secondaryLoginActionLists():Array {
    return _secondaryLoginActionLists;
  }

  public function set secondaryLoginActionLists(value:Array):void {
    _secondaryLoginActionLists = value;
  }
}
}
