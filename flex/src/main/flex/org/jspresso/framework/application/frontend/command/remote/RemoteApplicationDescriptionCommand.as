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

import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.util.gui.Dimension;

[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteApplicationDescriptionCommand")]
public class RemoteApplicationDescriptionCommand extends RemoteCommand {

  private var _applicationName:String;
  private var _applicationDescription:String;

  public function RemoteApplicationDescriptionCommand() {
    //default constructor.
  }

  public function get applicationName():String {
    return _applicationName;
  }

  public function set applicationName(value:String):void {
    _applicationName = value;
  }

  public function get applicationDescription():String {
    return _applicationDescription;
  }

  public function set applicationDescription(value:String):void {
    _applicationDescription = value;
  }
}
}
