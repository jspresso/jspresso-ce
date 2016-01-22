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


package org.jspresso.framework.state.remote {

import flash.events.Event;

import mx.collections.ListCollectionView;
import mx.events.PropertyChangeEvent;
import mx.events.PropertyChangeEventKind;

[Bindable]
[RemoteClass(alias="org.jspresso.framework.state.remote.RemoteCompositeValueState")]
public dynamic class RemoteCompositeValueState extends RemoteValueState {

  private var _children:ListCollectionView;
  private var _description:String;
  private var _iconImageUrl:String;
  private var _leadingIndex:int;
  private var _selectedIndices:Array;

  public function RemoteCompositeValueState() {
    //default constructor.
  }

  public function set children(value:ListCollectionView):void {
    _children = value;
  }

  public function get children():ListCollectionView {
    return _children;
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

  public function set leadingIndex(value:int):void {
    _leadingIndex = value;
  }

  public function get leadingIndex():int {
    return _leadingIndex;
  }

  public function set selectedIndices(value:Array):void {
    _selectedIndices = value;
  }

  public function get selectedIndices():Array {
    return _selectedIndices;
  }

  public function notifyChildrenChanged():void {
    this.dispatchEvent(new PropertyChangeEvent("propertyChange", true, true, PropertyChangeEventKind.UPDATE, "children",
                                               null, _children, this) as Event);
  }

  public function notifySelectionChanged():void {
    this.dispatchEvent(new PropertyChangeEvent("propertyChange", true, true, PropertyChangeEventKind.UPDATE,
                                               "selectedIndices", null, selectedIndices, this) as Event);
  }
}
}
