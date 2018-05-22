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


[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteMapChangedCommand")]
public class RemoteMapChangedCommand extends RemoteCommand {

  private var _mapId:String;
  private var _zoom:Number;

  public function RemoteMapChangedCommand() {
    //default constructor.
  }


  public function get mapId():String {
    return _mapId;
  }

  public function set mapId(value:String):void {
    _mapId = value;
  }

  public function get zoom():Number {
    return _zoom;
  }

  public function set zoom(value:Number):void {
    _zoom = value;
  }
}
}
