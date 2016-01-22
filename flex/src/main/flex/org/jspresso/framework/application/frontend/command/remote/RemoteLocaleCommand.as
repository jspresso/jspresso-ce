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


[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand")]
public class RemoteLocaleCommand extends RemoteCommand {

  public function RemoteLocaleCommand() {
    //default constructor.
  }

  private var _language:String;
  private var _datePattern:String;
  private var _firstDayOfWeek:int;
  private var _decimalSeparator:String;
  private var _thousandsSeparator:String;
  private var _translations:Object;

  public function set language(value:String):void {
    _language = value;
  }

  public function get language():String {
    return _language;
  }

  public function get datePattern():String {
    return _datePattern;
  }

  public function set datePattern(value:String):void {
    _datePattern = value;
  }

  public function get translations():Object {
    return _translations;
  }

  public function set translations(value:Object):void {
    _translations = value;
  }

  public function get firstDayOfWeek():int {
    return _firstDayOfWeek;
  }

  public function set firstDayOfWeek(value:int):void {
    _firstDayOfWeek = value;
  }


  public function get decimalSeparator():String {
    return _decimalSeparator;
  }

  public function set decimalSeparator(value:String):void {
    _decimalSeparator = value;
  }

  public function get thousandsSeparator():String {
    return _thousandsSeparator;
  }

  public function set thousandsSeparator(value:String):void {
    _thousandsSeparator = value;
  }
}
}
