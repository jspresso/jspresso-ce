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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RDateField")]
public class RDateField extends RComponent {

  private var _type:String;
  private var _secondsAware:Boolean;
  private var _millisecondsAware:Boolean;
  private var _formatPattern:String;

  public function RDateField() {
    //default constructor.
  }

  public function set type(value:String):void {
    _type = value;
  }

  public function get type():String {
    return _type;
  }

  public function get secondsAware():Boolean {
    return _secondsAware;
  }

  public function set secondsAware(value:Boolean):void {
    _secondsAware = value;
  }

  public function get millisecondsAware():Boolean {
    return _millisecondsAware;
  }

  public function set millisecondsAware(value:Boolean):void {
    _millisecondsAware = value;
  }

  public function get formatPattern():String {
    return _formatPattern;
  }

  public function set formatPattern(value:String):void {
    _formatPattern = value;
  }
}
}
