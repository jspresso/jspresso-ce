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


package org.jspresso.framework.util.lang {


[RemoteClass(alias="org.jspresso.framework.util.lang.DateDto")]
public class DateDto {

  private var _year:int;
  private var _month:int;
  private var _date:int;
  private var _hour:int;
  private var _minute:int;
  private var _second:int;
  private var _millisecond:int;

  public function DateDto() {
    //default constructor.
  }

  public function get year():int {
    return _year;
  }

  public function set year(value:int):void {
    _year = value;
  }

  public function get month():int {
    return _month;
  }

  public function set month(value:int):void {
    _month = value;
  }

  public function get date():int {
    return _date;
  }

  public function set date(value:int):void {
    _date = value;
  }

  public function get hour():int {
    return _hour;
  }

  public function set hour(value:int):void {
    _hour = value;
  }

  public function get minute():int {
    return _minute;
  }

  public function set minute(value:int):void {
    _minute = value;
  }

  public function get second():int {
    return _second;
  }

  public function set second(value:int):void {
    _second = value;
  }

  public function get millisecond():int {
    return _millisecond;
  }

  public function set millisecond(value:int):void {
    _millisecond = value;
  }
}
}
