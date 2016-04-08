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
[RemoteClass(alias="org.jspresso.framework.gui.remote.RImageActionEvent")]
public class RImageActionEvent extends RActionEvent {
  
  private var _width:int;
  private var _height:int;
  private var _x:int;
  private var _y:int;

  public function RImageActionEvent() {
    //default constructor.
  }

  public function get width():int {
    return _width;
  }

  public function set width(value:int):void {
    _width = value;
  }

  public function get height():int {
    return _height;
  }

  public function set height(value:int):void {
    _height = value;
  }

  public function get x():int {
    return _x;
  }

  public function set x(value:int):void {
    _x = value;
  }

  public function get y():int {
    return _y;
  }

  public function set y(value:int):void {
    _y = value;
  }
}
}
