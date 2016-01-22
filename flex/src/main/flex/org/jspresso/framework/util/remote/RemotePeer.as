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


package org.jspresso.framework.util.remote {


[RemoteClass(alias="org.jspresso.framework.util.remote.RemotePeer")]
public class RemotePeer implements IRemotePeer {

  private var _guid:String;
  private var _permId:String;

  public function RemotePeer() {
    //default constructor.
  }

  public function set guid(value:String):void {
    _guid = value;
  }

  public function get guid():String {
    return _guid;
  }

  public function set permId(value:String):void {
    _permId = value;
  }

  public function get permId():String {
    return _permId;
  }
}
}
