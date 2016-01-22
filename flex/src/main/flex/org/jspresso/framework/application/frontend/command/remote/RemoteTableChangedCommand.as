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


[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteTableChangedCommand")]
public class RemoteTableChangedCommand extends RemoteCommand {

  private var _tableId:String;
  private var _columnIds:Array;
  private var _columnWidths:Array;

  public function RemoteTableChangedCommand() {
    //default constructor.
  }

  public function get tableId():String {
    return _tableId;
  }

  public function set tableId(value:String):void {
    _tableId = value;
  }

  public function get columnIds():Array {
    return _columnIds;
  }

  public function set columnIds(value:Array):void {
    _columnIds = value;
  }

  public function get columnWidths():Array {
    return _columnWidths;
  }

  public function set columnWidths(value:Array):void {
    _columnWidths = value;
  }


}
}
