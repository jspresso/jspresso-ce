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

import org.jspresso.framework.util.remote.RemotePeer;

[Bindable]
[RemoteClass(alias="org.jspresso.framework.gui.remote.RAction")]
public class RAction extends RemotePeer {

  private var _acceleratorAsString:String;
  private var _description:String;
  private var _enabled:Boolean;
  private var _icon:RIcon;
  private var _styleName:String;
  private var _mnemonicAsString:String;
  private var _name:String;

  public function RAction() {
    //default constructor.
  }

  public function set acceleratorAsString(value:String):void {
    _acceleratorAsString = value;
  }

  public function get acceleratorAsString():String {
    return _acceleratorAsString;
  }

  public function set description(value:String):void {
    _description = value;
  }

  public function get description():String {
    return _description;
  }

  public function set enabled(value:Boolean):void {
    _enabled = value;
  }

  public function get enabled():Boolean {
    return _enabled;
  }

  public function set icon(value:RIcon):void {
    _icon = value;
  }

  public function get icon():RIcon {
    return _icon;
  }

  public function set mnemonicAsString(value:String):void {
    _mnemonicAsString = value;
  }

  public function get mnemonicAsString():String {
    return _mnemonicAsString;
  }

  public function set name(value:String):void {
    _name = value;
  }

  public function get name():String {
    return _name;
  }

  public function get styleName():String {
    return _styleName;
  }

  public function set styleName(value:String):void {
    _styleName = value;
  }

}
}
