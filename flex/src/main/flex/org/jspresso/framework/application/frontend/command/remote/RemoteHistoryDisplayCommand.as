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

[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand")]
public class RemoteHistoryDisplayCommand extends RemoteCommand {

  private var _snapshotId:String;
  private var _name:String;

  public function RemoteHistoryDisplayCommand() {
    //default constructor.
  }

  public function get snapshotId():String {
    return _snapshotId;
  }

  public function set snapshotId(value:String):void {
    _snapshotId = value;
  }

  public function get name():String {
    return _name;
  }

  public function set name(value:String):void {
    _name = value;
  }


}
}
