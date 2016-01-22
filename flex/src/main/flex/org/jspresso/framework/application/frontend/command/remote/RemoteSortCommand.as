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


[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand")]
public class RemoteSortCommand extends RemoteCommand {

  private var _orderingProperties:Object;
  private var _viewStateGuid:String;
  private var _viewStatePermId:String;

  public function RemoteSortCommand() {
    //default constructor.
  }

  public function set orderingProperties(value:Object):void {
    _orderingProperties = value;
  }

  public function get orderingProperties():Object {
    return _orderingProperties;
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
