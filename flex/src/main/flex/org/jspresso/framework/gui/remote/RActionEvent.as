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


package org.jspresso.framework.gui.remote {

[Bindable]
[RemoteClass(alias="org.jspresso.framework.gui.remote.RActionEvent")]
public class RActionEvent {

  private var _actionCommand:String;
  private var _viewStateGuid:String;
  private var _viewStatePermId:String;

  public function RActionEvent() {
    //default constructor.
  }

  public function set actionCommand(value:String):void {
    _actionCommand = value;
  }

  public function get actionCommand():String {
    return _actionCommand;
  }

  public function set viewStateGuid(value:String):void {
    _viewStateGuid = value;
  }

  public function get viewStateGuid():String {
    return _viewStateGuid;
  }

  public function set viewStatePermId(value:String):void {
    _viewStatePermId = value;
  }

  public function get viewStatePermId():String {
    return _viewStatePermId;
  }
}
}
