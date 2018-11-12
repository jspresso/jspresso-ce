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


package org.jspresso.framework.util.gui {


[RemoteClass(alias="org.jspresso.framework.util.gui.CellConstraints")]
public class CellConstraints {

  private var _column:int;
  private var _height:int;
  private var _heightResizable:Boolean;
  private var _row:int;
  private var _width:int;
  private var _widthResizable:Boolean;
  private var _fillWidth:Boolean;
  private var _fillHeight:Boolean;
  private var _horizontalAlignment:String;
  private var _verticalAlignment:String;

  public function CellConstraints() {
    //default constructor.
  }

  public function set column(value:int):void {
    _column = value;
  }

  public function get column():int {
    return _column;
  }

  public function set height(value:int):void {
    _height = value;
  }

  public function get height():int {
    return _height;
  }

  public function set heightResizable(value:Boolean):void {
    _heightResizable = value;
  }

  public function get heightResizable():Boolean {
    return _heightResizable;
  }

  public function set row(value:int):void {
    _row = value;
  }

  public function get row():int {
    return _row;
  }

  public function set width(value:int):void {
    _width = value;
  }

  public function get width():int {
    return _width;
  }

  public function set widthResizable(value:Boolean):void {
    _widthResizable = value;
  }

  public function get widthResizable():Boolean {
    return _widthResizable;
  }

  public function get fillWidth():Boolean {
    return _fillWidth;
  }

  public function set fillWidth(value:Boolean):void {
    _fillWidth = value;
  }

  public function get fillHeight():Boolean {
    return _fillHeight;
  }

  public function set fillHeight(value:Boolean):void {
    _fillHeight = value;
  }

  public function get horizontalAlignment():String {
    return _horizontalAlignment;
  }

  public function set horizontalAlignment(value:String):void {
    _horizontalAlignment = value;
  }

  public function get verticalAlignment():String {
    return _verticalAlignment;
  }

  public function set verticalAlignment(value:String):void {
    _verticalAlignment = value;
  }
}
}
