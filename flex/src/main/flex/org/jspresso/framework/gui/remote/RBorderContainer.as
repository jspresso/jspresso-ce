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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RBorderContainer")]
public class RBorderContainer extends RContainer {

  private var _center:RComponent;
  private var _east:RComponent;
  private var _north:RComponent;
  private var _south:RComponent;
  private var _west:RComponent;

  public function RBorderContainer() {
    //default constructor.
  }

  public function set center(value:RComponent):void {
    _center = value;
  }

  public function get center():RComponent {
    return _center;
  }

  public function set east(value:RComponent):void {
    _east = value;
  }

  public function get east():RComponent {
    return _east;
  }

  public function set north(value:RComponent):void {
    _north = value;
  }

  public function get north():RComponent {
    return _north;
  }

  public function set south(value:RComponent):void {
    _south = value;
  }

  public function get south():RComponent {
    return _south;
  }

  public function set west(value:RComponent):void {
    _west = value;
  }

  public function get west():RComponent {
    return _west;
  }
}
}
