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

import org.jspresso.framework.util.gui.Dimension;

[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand")]
public class RemoteAbstractDialogCommand extends RemoteCommand {

  private var _actions:Array;
  private var _title:String;
  private var _useCurrent:Boolean;
  private var _dimension:Dimension;

  public function RemoteAbstractDialogCommand() {
    //default constructor.
  }

  public function set actions(value:Array):void {
    _actions = value;
  }

  public function get actions():Array {
    return _actions;
  }

  public function set title(value:String):void {
    _title = value;
  }

  public function get title():String {
    return _title;
  }

  public function set useCurrent(value:Boolean):void {
    _useCurrent = value;
  }

  public function get useCurrent():Boolean {
    return _useCurrent;
  }

  public function set dimension(value:Dimension):void {
    _dimension = value;
  }

  public function get dimension():Dimension {
    return _dimension;
  }
}
}
