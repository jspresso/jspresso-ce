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

import mx.core.UIComponent;

import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Font;
import org.jspresso.framework.util.remote.RemotePeer;

[RemoteClass(alias="org.jspresso.framework.gui.remote.RComponent")]
public class RComponent extends RemotePeer implements IRemoteStateOwner {

  private var _actionLists:Array;
  private var _secondaryActionLists:Array;
  private var _background:String;
  private var _backgroundState:RemoteValueState;
  private var _borderType:String;
  private var _font:Font;
  private var _fontState:RemoteValueState;
  private var _foreground:String;
  private var _foregroundState:RemoteValueState;
  private var _icon:RIcon;
  private var _label:String;
  private var _labelState:RemoteValueState;
  private var _state:RemoteValueState;
  private var _toolTip:String;
  private var _toolTipState:RemoteValueState;
  private var _preferredSize:Dimension;
  private var _styleName:String;

  private var _peer:UIComponent;

  public function RComponent() {
    //default constructor.
  }

  public function set actionLists(value:Array):void {
    _actionLists = value;
  }

  public function get actionLists():Array {
    return _actionLists;
  }

  public function set background(value:String):void {
    _background = value;
  }

  public function get background():String {
    return _background;
  }

  public function set borderType(value:String):void {
    _borderType = value;
  }

  public function get borderType():String {
    return _borderType;
  }

  public function set font(value:Font):void {
    _font = value;
  }

  public function get font():Font {
    return _font;
  }

  public function set foreground(value:String):void {
    _foreground = value;
  }

  public function get foreground():String {
    return _foreground;
  }

  public function set icon(value:RIcon):void {
    _icon = value;
  }

  public function get icon():RIcon {
    return _icon;
  }

  public function set label(value:String):void {
    _label = value;
  }

  public function get label():String {
    return _label;
  }

  public function set state(value:RemoteValueState):void {
    _state = value;
  }

  public function get state():RemoteValueState {
    return _state;
  }

  public function set toolTip(value:String):void {
    _toolTip = value;
  }

  public function get toolTip():String {
    return _toolTip;
  }

  public function get toolTipState():RemoteValueState {
    return _toolTipState;
  }

  public function set toolTipState(value:RemoteValueState):void {
    _toolTipState = value;
  }

  public function set preferredSize(value:Dimension):void {
    _preferredSize = value;
  }

  public function get preferredSize():Dimension {
    return _preferredSize;
  }

  public function get secondaryActionLists():Array {
    return _secondaryActionLists;
  }

  public function set secondaryActionLists(value:Array):void {
    _secondaryActionLists = value;
  }

  public function assignPeer(value:UIComponent):void {
    _peer = value;
  }

  public function retrievePeer():UIComponent {
    return _peer;
  }

  public function get styleName():String {
    return _styleName;
  }

  public function set styleName(value:String):void {
    _styleName = value;
  }

  public function get backgroundState():RemoteValueState {
    return _backgroundState;
  }

  public function set backgroundState(value:RemoteValueState):void {
    _backgroundState = value;
  }

  public function get foregroundState():RemoteValueState {
    return _foregroundState;
  }

  public function set foregroundState(value:RemoteValueState):void {
    _foregroundState = value;
  }

  public function get labelState():RemoteValueState {
    return _labelState;
  }

  public function set labelState(value:RemoteValueState):void {
    _labelState = value;
  }

  public function get fontState():RemoteValueState {
    return _fontState;
  }

  public function set fontState(value:RemoteValueState):void {
    _fontState = value;
  }


}
}
