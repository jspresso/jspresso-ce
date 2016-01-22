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


[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand")]
public class RemoteStartCommand extends RemoteCommand {

  private var _language:String;
  private var _timezoneOffset:int;
  private var _keysToTranslate:Array;
  private var _version:String;
  private var _clientType:String;

  public function RemoteStartCommand() {
    //default constructor.
  }

  public function get language():String {
    return _language;
  }

  public function set language(value:String):void {
    _language = value;
  }

  public function get timezoneOffset():int {
    return _timezoneOffset;
  }

  public function set timezoneOffset(value:int):void {
    _timezoneOffset = value;
  }

  public function get keysToTranslate():Array {
    return _keysToTranslate;
  }

  public function set keysToTranslate(value:Array):void {
    _keysToTranslate = value;
  }

  public function get version():String {
    return _version;
  }

  public function set version(value:String):void {
    _version = value;
  }


  public function get clientType():String {
    return _clientType;
  }

  public function set clientType(value:String):void {
    _clientType = value;
  }
}
}
