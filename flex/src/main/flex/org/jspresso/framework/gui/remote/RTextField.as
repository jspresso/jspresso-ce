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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RTextField")]
public class RTextField extends RTextComponent {

  private var _horizontalAlignment:String;
  private var _characterAction:RAction;

  public function RTextField() {
    //default constructor.
  }

  public function get horizontalAlignment():String {
    return _horizontalAlignment;
  }

  public function set horizontalAlignment(value:String):void {
    _horizontalAlignment = value;
  }

  public function get characterAction():RAction {
    return _characterAction;
  }

  public function set characterAction(value:RAction):void {
    _characterAction = value;
  }
}
}
