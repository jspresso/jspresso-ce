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

package org.jspresso.framework.view.flex {

import flash.display.DisplayObject;
import flash.events.FocusEvent;
import flash.text.TextField;

import mx.binding.utils.BindingUtils;
import mx.binding.utils.ChangeWatcher;
import mx.containers.Canvas;
import mx.controls.CheckBox;
import mx.controls.TextInput;
import mx.core.Container;
import mx.core.ScrollPolicy;
import mx.core.UIComponent;

import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;

public class RemoteValueDgItemEditor extends Canvas implements IColumnIndexProvider {

  private var _valueChangeListener:ChangeWatcher;
  private var _editor:UIComponent;
  private var _state:RemoteValueState;
  private var _index:int;

  public function RemoteValueDgItemEditor() {
    _index = -1;
    horizontalScrollPolicy = ScrollPolicy.OFF;
    verticalScrollPolicy = ScrollPolicy.OFF;
    percentWidth = 100.0;
    percentHeight = 100.0;
  }

  public function set editor(value:UIComponent):void {
    _editor = value;
    setupChildren();
  }

  public function get editor():UIComponent {
    return _editor;
  }

  public function set state(value:RemoteValueState):void {
    _state = value;
  }

  public function get state():RemoteValueState {
    return _state;
  }

  public function set index(value:int):void {
    _index = value;
  }

  public function get index():int {
    return _index;
  }

  private function setupChildren():void {
    removeAllChildren();
    if(_editor is EnhancedCheckBox) {
      _editor.setStyle("horizontalCenter", 0);
      (_editor as EnhancedCheckBox).forceCentered = true;
    } else {
      _editor.percentWidth = 100.0;
    }
    addChild(_editor);
  }

  override public function set data(value:Object):void {
    super.data = value;
    var cellValueState:RemoteValueState;
    if (index != -1 && value is RemoteCompositeValueState) {
      cellValueState = (value as RemoteCompositeValueState).children[index] as RemoteValueState;
    } else if (value is RemoteValueState) {
      cellValueState = value as RemoteValueState;
    }
    if (_valueChangeListener != null) {
      _valueChangeListener.reset(cellValueState);
      _state.value = cellValueState.value;
    } else {
      _valueChangeListener = BindingUtils.bindProperty(_state, "value", cellValueState, "value", true);
    }
    if (_state.value == null || _state.value == "") {
      var tf:UIComponent;
      if (_editor is Container) {
        tf = (_editor as Container).getChildByName("tf") as UIComponent;
      }
      if (tf is TextInput) {
        (tf as TextInput).text = null;
      }
    }
  }

  public function cleanup():void {
    if (_valueChangeListener != null) {
      _valueChangeListener.unwatch();
    }
  }

  override public function setFocus():void {
    var tf:UIComponent;
    if (_editor is Container) {
      tf = (_editor as Container).getChildByName("tf") as UIComponent;
    }
    if (tf != null) {
      tf.setFocus();
    } else {
      _editor.setFocus();
    }
  }

  override public function addEventListener(type:String, listener:Function, useCapture:Boolean = false,
                                            priority:int = 0, useWeakReference:Boolean = false):void {
    // prevent bogus DataGrid to install its bogus focus out listener...
    if (type != FocusEvent.FOCUS_OUT) {
      super.addEventListener(type, listener, useCapture, priority, useWeakReference);
    }
  }
}
}
