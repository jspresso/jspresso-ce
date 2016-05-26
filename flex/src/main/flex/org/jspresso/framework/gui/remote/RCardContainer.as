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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RCardContainer")]
public class RCardContainer extends RContainer {

  private var _cardNames:Array;
  private var _cards:Array;

  public function RCardContainer() {
    //default constructor.
  }

  public function set cardNames(value:Array):void {
    _cardNames = value;
  }

  public function get cardNames():Array {
    return _cardNames;
  }

  public function set cards(value:Array):void {
    _cards = value;
  }

  public function get cards():Array {
    return _cards;
  }
}
}
