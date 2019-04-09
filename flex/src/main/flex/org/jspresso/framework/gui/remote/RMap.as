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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RMap")]
public class RMap extends RComponent {

  private var _defaultZoom:Number;
  private var _markerAction:RAction;
  private var _routeAction:RAction;
  private var _zoneAction:RAction;

  public function RMap() {
    //default constructor.
  }

  public function get defaultZoom():Number {
    return _defaultZoom;
  }

  public function set defaultZoom(value:Number):void {
    _defaultZoom = value;
  }

  public function get markerAction():RAction {
    return _markerAction;
  }

  public function set markerAction(value:RAction):void {
    _markerAction = value;
  }

  public function get routeAction():RAction {
    return _routeAction;
  }

  public function set routeAction(value:RAction):void {
    _routeAction = value;
  }

  public function get zoneAction():RAction {
    return _zoneAction;
  }

  public function set zoneAction(value:RAction):void {
    _zoneAction = value;
  }
}
}
