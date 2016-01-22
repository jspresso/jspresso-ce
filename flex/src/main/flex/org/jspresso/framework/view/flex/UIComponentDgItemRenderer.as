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

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.binding.utils.ChangeWatcher;
import mx.controls.DataGrid;
import mx.controls.listClasses.BaseListData;
import mx.controls.listClasses.IDropInListItemRenderer;

import org.jspresso.framework.action.IActionHandler;

import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.gui.Font;

public class UIComponentDgItemRenderer extends RemoteValueDgItemEditor implements IDropInListItemRenderer {

  private var _viewFactory:DefaultFlexViewFactory;
  private var _actionHandler:IActionHandler;
  private var _remoteComponent:RComponent;
  private var _toolTipIndex:int;
  private var _backgroundIndex:int;
  private var _foregroundIndex:int;
  private var _fontIndex:int;
  private var _listData:BaseListData;

  private var _valueChangeListener:ChangeWatcher;
  private var _backgroundChangeListener:ChangeWatcher;
  private var _foregroundChangeListener:ChangeWatcher;
  private var _fontChangeListener:ChangeWatcher;
  private var _toolTipListener:ChangeWatcher;

  private var _writabilityListener:ChangeWatcher;

  public function UIComponentDgItemRenderer() {
    //default constructor.
    addEventListener(MouseEvent.MOUSE_OVER, function (event:MouseEvent):void {
      var dg:DataGrid = listData.owner as DataGrid;
      // Even if the mouse enters or leaves the editing cell, the current view state GUID should
      // stay on the editing cell. See bug #1217.
      if (!dg.editedItemPosition) {
        var cellValueState:RemoteValueState = getCellValueState();
        _actionHandler.setCurrentViewStateGuid(dg, cellValueState.guid, cellValueState.permId);
      }
    });
    addEventListener(MouseEvent.MOUSE_OUT, function (event:MouseEvent):void {
      var dg:DataGrid = listData.owner as DataGrid;
      // Even if the mouse enters or leaves the editing cell, the current view state GUID should
      // stay on the editing cell. See bug #1217.
      if (!dg.editedItemPosition) {
        var cellValueState:RemoteValueState = getCellValueState();
        _actionHandler.setCurrentViewStateGuid(dg, null, null);
      }
    });
  }

  public function set viewFactory(value:DefaultFlexViewFactory):void {
    _viewFactory = value;
    updateComponents();
  }

  public function get viewFactory():DefaultFlexViewFactory {
    return _viewFactory;
  }

  public function set remoteComponent(value:RComponent):void {
    _remoteComponent = value;
    updateComponents();
  }

  public function get remoteComponent():RComponent {
    return _remoteComponent;
  }

  public function set toolTipIndex(value:int):void {
    _toolTipIndex = value;
  }

  public function get toolTipIndex():int {
    return _toolTipIndex;
  }

  private function updateComponents():void {
    if (viewFactory != null && remoteComponent != null) {
      if (state == null || editor == null) {
        state = new RemoteValueState();
        remoteComponent.state = state;
        editor = viewFactory.createComponent(remoteComponent, false);
        BindingUtils.bindSetter(apply, state, "value", true);
      }
    }
  }

  private function redraw(value:Object):void {
    invalidateDisplayList();
  }

  override public function set data(value:Object):void {
    super.data = value;
    var cellValueState:RemoteValueState = getCellValueState();
    if (_valueChangeListener != null) {
      _valueChangeListener.reset(cellValueState);
      refresh(cellValueState.value);
    } else {
      _valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value", true);
    }
    if (_writabilityListener != null) {
      _writabilityListener.reset(cellValueState);
      refreshWritability(cellValueState.writable);
    } else {
      _writabilityListener = BindingUtils.bindSetter(refreshWritability, cellValueState, "writable", true);
    }
    if (toolTipIndex >= 0) {
      var toolTipState:RemoteValueState = ((data as RemoteCompositeValueState).children[toolTipIndex]
          as RemoteValueState);
      if (_toolTipListener != null) {
        _toolTipListener.reset(toolTipState);
        refreshToolTip(toolTipState.value);
      } else {
        _toolTipListener = BindingUtils.bindSetter(refreshToolTip, toolTipState, "value", true);
      }
    }
    if (backgroundIndex >= 0) {
      var backgroundState:RemoteValueState = ((data as RemoteCompositeValueState).children[backgroundIndex]
          as RemoteValueState);
      if (_backgroundChangeListener != null) {
        _backgroundChangeListener.reset(backgroundState);
        redraw(backgroundState.value);
      } else {
        _backgroundChangeListener = BindingUtils.bindSetter(redraw, backgroundState, "value", true);
      }
    }
    if (foregroundIndex >= 0) {
      var foregroundState:RemoteValueState = ((data as RemoteCompositeValueState).children[foregroundIndex]
          as RemoteValueState);
      if (_foregroundChangeListener != null) {
        _foregroundChangeListener.reset(foregroundState);
        redraw(foregroundState.value);
      } else {
        _foregroundChangeListener = BindingUtils.bindSetter(redraw, foregroundState, "value", true);
      }
    }
    if (fontIndex >= 0) {
      var fontState:RemoteValueState = ((data as RemoteCompositeValueState).children[fontIndex] as RemoteValueState);
      if (_fontChangeListener != null) {
        _fontChangeListener.reset(fontState);
        redraw(fontState.value);
      } else {
        _fontChangeListener = BindingUtils.bindSetter(redraw, fontState, "value", true);
      }
    }
  }

  protected function getCellValueState():RemoteValueState {
    var cellValueState:RemoteValueState;
    var value:Object = data;
    if (index != -1 && value is RemoteCompositeValueState) {
      cellValueState = (value as RemoteCompositeValueState).children[index] as RemoteValueState;
    } else if (value is RemoteValueState) {
      cellValueState = value as RemoteValueState;
    }
    return cellValueState;
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    if (listData.owner is DataGrid) {
      if (backgroundIndex >= 0) {
        var backgroundValue:Object = ((data as RemoteCompositeValueState).children[backgroundIndex]
            as RemoteValueState).value;
        if (backgroundValue) {
          setStyle("backgroundColor", backgroundValue);
          setStyle("backgroundAlpha", DefaultFlexViewFactory.getAlphaFromArgb(backgroundValue as String));
        } else {
          setStyle("backgroundColor", null);
          setStyle("backgroundAlpha", null);
        }
      }
      if (foregroundIndex >= 0) {
        var foregroundValue:Object = ((data as RemoteCompositeValueState).children[foregroundIndex]
            as RemoteValueState).value;
        if (foregroundValue) {
          setStyle("color", foregroundValue);
          setStyle("alpha", DefaultFlexViewFactory.getAlphaFromArgb(foregroundValue as String));
        } else {
          setStyle("color", null);
          setStyle("alpha", null);
        }
      }
      if (fontIndex >= 0) {
        var fontValue:Object = ((data as RemoteCompositeValueState).children[fontIndex] as RemoteValueState).value;
        if (fontValue is Font) {
          if ((fontValue as Font).name) {
            setStyle("fontFamily", (fontValue as Font).name);
          }
          if ((fontValue as Font).size > 0) {
            setStyle("fontSize", (fontValue as Font).size);
          }
          if ((fontValue as Font).italic) {
            setStyle("fontStyle", "italic");
          }
          if ((fontValue as Font).bold) {
            setStyle("fontWeight", "bold");
          }
        } else {
          setStyle("fontFamily", null);
          setStyle("fontSize", null);
          setStyle("fontStyle", null);
          setStyle("fontWeight", null);
        }
      }
    }
    super.updateDisplayList(unscaledWidth, unscaledHeight);
  }

  protected function refresh(value:Object):void {
    state.value = value;
  }

  protected function apply(value:Object):void {
    var cellValueState:RemoteValueState = getCellValueState();
    if (cellValueState) {
      cellValueState.value = value;
    }
  }

  protected function refreshWritability(value:Boolean):void {
    state.writable = value && (!(listData.owner is DataGrid) || (listData.owner as DataGrid).editable);
  }

  protected function refreshToolTip(toolTipValue:Object):void {
    if (toolTipValue != null) {
      toolTip = toolTipValue.toString();
    } else {
      toolTip = null;
    }
  }

  public function get listData():BaseListData {
    return _listData;
  }

  public function set listData(value:BaseListData):void {
    _listData = value;
    invalidateProperties();
  }

  public function get backgroundIndex():int {
    return _backgroundIndex;
  }

  public function set backgroundIndex(value:int):void {
    _backgroundIndex = value;
  }

  public function get foregroundIndex():int {
    return _foregroundIndex;
  }

  public function set foregroundIndex(value:int):void {
    _foregroundIndex = value;
  }

  public function get fontIndex():int {
    return _fontIndex;
  }

  public function set fontIndex(value:int):void {
    _fontIndex = value;
  }

  public function set actionHandler(value:IActionHandler):void {
    _actionHandler = value;
  }
}
}
