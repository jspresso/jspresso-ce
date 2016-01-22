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

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.remote.RemotePeer;

[RemoteClass(alias="org.jspresso.framework.gui.remote.RIcon")]
public class RIcon extends RemotePeer {

  private var _dimension:Dimension;
  private var _imageUrlSpec:String;

  public function RIcon() {
    //default constructor.
  }

  public function set dimension(value:Dimension):void {
    _dimension = value;
  }

  public function get dimension():Dimension {
    return _dimension;
  }

  public function set imageUrlSpec(value:String):void {
    _imageUrlSpec = value;
  }

  public function get imageUrlSpec():String {
    return _imageUrlSpec;
  }
}
}
