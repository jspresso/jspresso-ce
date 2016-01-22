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


package org.jspresso.framework.state.remote {

import org.jspresso.framework.util.remote.RemotePeer;

[Bindable]
[RemoteClass(alias="org.jspresso.framework.state.remote.RemoteValueState")]
public dynamic class RemoteValueState extends RemotePeer {

  private var _readable:Boolean;
  private var _value:Object;
  private var _writable:Boolean;
  private var _parent:RemoteCompositeValueState;

  public function RemoteValueState() {
    //default constructor.
  }

  public function set readable(value:Boolean):void {
    _readable = value;
  }

  public function get readable():Boolean {
    return _readable;
  }

  public function set value(value:Object):void {
    _value = value;
  }

  public function get value():Object {
    return _value;
  }

  public function set writable(value:Boolean):void {
    _writable = value;
  }

  public function get writable():Boolean {
    return _writable;
  }

  public function get parent():RemoteCompositeValueState {
    return _parent;
  }

  public function set parent(value:RemoteCompositeValueState):void {
    _parent = value;
  }

}
}
