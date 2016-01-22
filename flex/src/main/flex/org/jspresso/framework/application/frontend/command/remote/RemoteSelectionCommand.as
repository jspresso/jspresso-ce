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


[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand")]
public class RemoteSelectionCommand extends RemoteCommand {

  private var _leadingIndex:int;
  private var _selectedIndices:Array;

  public function RemoteSelectionCommand() {
    //default constructor.
  }

  public function set leadingIndex(value:int):void {
    _leadingIndex = value;
  }

  public function get leadingIndex():int {
    return _leadingIndex;
  }

  public function set selectedIndices(value:Array):void {
    _selectedIndices = value;
  }

  public function get selectedIndices():Array {
    return _selectedIndices;
  }
}
}
