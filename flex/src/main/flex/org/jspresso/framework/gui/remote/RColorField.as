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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RColorField")]
public class RColorField extends RComponent {

  private var _defaultColor:String;
  private var _resetEnabled:Boolean;

  public function RColorField() {
    //default constructor.
  }

  public function set defaultColor(value:String):void {
    _defaultColor = value;
  }

  public function get defaultColor():String {
    return _defaultColor;
  }

  public function get resetEnabled():Boolean {
    return _resetEnabled;
  }

  public function set resetEnabled(value:Boolean):void {
    _resetEnabled = value;
  }

}
}
