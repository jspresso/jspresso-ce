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


[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand")]
public class RemoteValueCommand extends RemoteCommand {

  private var _description:String;
  private var _iconImageUrl:String;
  private var _value:Object;
  private var _valueAsObject:Object;

  public function RemoteValueCommand() {
    //default constructor.
  }

  public function set description(value:String):void {
    _description = value;
  }

  public function get description():String {
    return _description;
  }

  public function set iconImageUrl(value:String):void {
    _iconImageUrl = value;
  }

  public function get iconImageUrl():String {
    return _iconImageUrl;
  }

  public function set value(value:Object):void {
    _value = value;
  }

  public function get value():Object {
    return _value;
  }

  public function set valueAsObject(value:Object):void {
    _valueAsObject = value;
  }

  public function get valueAsObject():Object {
    return _valueAsObject;
  }
}
}
