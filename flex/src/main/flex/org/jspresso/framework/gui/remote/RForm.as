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


[RemoteClass(alias="org.jspresso.framework.gui.remote.RForm")]
public class RForm extends RComponent {

  private var _columnCount:int;
  private var _elementWidths:Array;
  private var _elements:Array;
  private var _elementLabels:Array;
  private var _labelHorizontalPositions:Array;
  private var _labelsPosition:String;
  private var _verticallyScrollable:Boolean;
  private var _widthResizeable:Boolean;

  public function RForm() {
    //default constructor.
  }

  public function set columnCount(value:int):void {
    _columnCount = value;
  }

  public function get columnCount():int {
    return _columnCount;
  }

  public function set elementWidths(value:Array):void {
    _elementWidths = value;
  }

  public function get elementWidths():Array {
    return _elementWidths;
  }

  public function set elements(value:Array):void {
    _elements = value;
  }

  public function get elements():Array {
    return _elements;
  }

  public function set elementLabels(value:Array):void {
    _elementLabels = value;
  }

  public function get elementLabels():Array {
    return _elementLabels;
  }

  public function set labelsPosition(value:String):void {
    _labelsPosition = value;
  }

  public function get labelsPosition():String {
    return _labelsPosition;
  }

  public function get verticallyScrollable():Boolean {
    return _verticallyScrollable;
  }

  public function set verticallyScrollable(value:Boolean):void {
    _verticallyScrollable = value;
  }

  public function get labelHorizontalPositions():Array {
    return _labelHorizontalPositions;
  }

  public function set labelHorizontalPositions(value:Array):void {
    _labelHorizontalPositions = value;
  }

  public function get widthResizeable():Boolean {
    return _widthResizeable;
  }

  public function set widthResizeable(value:Boolean):void {
    _widthResizeable = value;
  }
}
}
