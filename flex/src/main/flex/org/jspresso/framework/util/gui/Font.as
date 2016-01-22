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


package org.jspresso.framework.util.gui {

[RemoteClass(alias="org.jspresso.framework.util.gui.Font")]
public class Font {

  private var _bold:Boolean;
  private var _italic:Boolean;
  private var _name:String;
  private var _size:int;

  public function Font() {
    //default constructor.
  }

  public function set bold(value:Boolean):void {
    _bold = value;
  }

  public function get bold():Boolean {
    return _bold;
  }

  public function set italic(value:Boolean):void {
    _italic = value;
  }

  public function get italic():Boolean {
    return _italic;
  }

  public function set name(value:String):void {
    _name = value;
  }

  public function get name():String {
    return _name;
  }

  public function set size(value:int):void {
    _size = value;
  }

  public function get size():int {
    return _size;
  }
}
}
