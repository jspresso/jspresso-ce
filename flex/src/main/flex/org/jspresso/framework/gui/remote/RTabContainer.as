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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RTabContainer")]
public class RTabContainer extends RContainer {

  private var _tabs:Array;
  private var _selectedIndex:int;

  public function RTabContainer() {
    //default constructor.
  }

  public function set tabs(value:Array):void {
    _tabs = value;
  }

  public function get tabs():Array {
    return _tabs;
  }

  [Bindable]
  public function get selectedIndex():int {
    return _selectedIndex;
  }

  public function set selectedIndex(value:int):void {
    _selectedIndex = value;
  }

}
}
