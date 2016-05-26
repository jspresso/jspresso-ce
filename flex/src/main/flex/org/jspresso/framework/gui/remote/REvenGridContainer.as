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


[RemoteClass(alias="org.jspresso.framework.gui.remote.REvenGridContainer")]
public class REvenGridContainer extends RContainer {

  private var _cells:Array;
  private var _drivingDimension:String;
  private var _drivingDimensionCellCount:int;

  public function REvenGridContainer() {
    //default constructor.
  }

  public function set cells(value:Array):void {
    _cells = value;
  }

  public function get cells():Array {
    return _cells;
  }

  public function set drivingDimension(value:String):void {
    _drivingDimension = value;
  }

  public function get drivingDimension():String {
    return _drivingDimension;
  }

  public function set drivingDimensionCellCount(value:int):void {
    _drivingDimensionCellCount = value;
  }

  public function get drivingDimensionCellCount():int {
    return _drivingDimensionCellCount;
  }
}
}
