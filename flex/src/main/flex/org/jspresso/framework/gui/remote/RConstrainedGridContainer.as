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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RConstrainedGridContainer")]
public class RConstrainedGridContainer extends RContainer {

  private var _cellConstraints:Array;
  private var _cells:Array;

  public function RConstrainedGridContainer() {
    //default constructor.
  }

  public function set cellConstraints(value:Array):void {
    _cellConstraints = value;
  }

  public function get cellConstraints():Array {
    return _cellConstraints;
  }

  public function set cells(value:Array):void {
    _cells = value;
  }

  public function get cells():Array {
    return _cells;
  }
}
}
