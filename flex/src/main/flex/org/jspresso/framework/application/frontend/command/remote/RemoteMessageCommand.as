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

import org.jspresso.framework.gui.remote.RIcon;

[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand")]
public class RemoteMessageCommand extends RemoteCommand {

  private var _message:String;
  private var _messageIcon:RIcon;
  private var _title:String;
  private var _titleIcon:RIcon;

  public function RemoteMessageCommand() {
    //default constructor.
  }

  public function set message(value:String):void {
    _message = value;
  }

  public function get message():String {
    return _message;
  }

  public function set messageIcon(value:RIcon):void {
    _messageIcon = value;
  }

  public function get messageIcon():RIcon {
    return _messageIcon;
  }

  public function set title(value:String):void {
    _title = value;
  }

  public function get title():String {
    return _title;
  }

  public function set titleIcon(value:RIcon):void {
    _titleIcon = value;
  }

  public function get titleIcon():RIcon {
    return _titleIcon;
  }
}
}
