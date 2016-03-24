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

import flash.display.Graphics;
import flash.events.KeyboardEvent;
import flash.events.TextEvent;
import flash.ui.Keyboard;

import mx.binding.utils.BindingUtils;
import mx.binding.utils.ChangeWatcher;
import mx.controls.DataGrid;
import mx.controls.listClasses.BaseListData;
import mx.controls.listClasses.ListData;
import mx.controls.listClasses.ListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.formatters.Formatter;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.gui.Font;
import org.jspresso.framework.util.html.HtmlUtil;

public class RemoteValueDgItemRenderer extends ListItemRenderer implements IColumnIndexProvider {

  private var _valueChangeListener:ChangeWatcher;
  private var _toolTipChangeListener:ChangeWatcher;
  private var _writabilityChangeListener:ChangeWatcher;
  private var _backgroundChangeListener:ChangeWatcher;
  private var _foregroundChangeListener:ChangeWatcher;
  private var _fontChangeListener:ChangeWatcher;
  private var _listData:BaseListData;
  private var _formatter:Formatter;
  private var _index:int;
  private var _toolTipIndex:int;
  private var _backgroundIndex:int;
  private var _foregroundIndex:int;
  private var _fontIndex:int;
  private var _forceSelectable:Boolean;
  private var _selectable:Boolean;
  private var _action:RAction;
  private var _actionHandler:IActionHandler;

  public function RemoteValueDgItemRenderer() {
    _index = -1;
    _toolTipIndex = -1;
    _backgroundIndex = -1;
    _foregroundIndex = -1;
    addEventListener(FlexEvent.CREATION_COMPLETE, function (event:FlexEvent):void {
      selectable = _selectable;
      // In order to fix jspresso-ce-113
      label.addEventListener(KeyboardEvent.KEY_DOWN, function (e:KeyboardEvent):void {
        if (label.selectable && (e.keyCode == Keyboard.UP || e.keyCode == Keyboard.DOWN || e.keyCode == Keyboard.LEFT
            || e.keyCode == Keyboard.RIGHT || e.keyCode == Keyboard.TAB)) {
          callLater(function ():void {
            if (parent is UIComponent) {
              (parent as UIComponent).setFocus();
            }
          });
        }
      });
    });
  }

  override public function set listData(value:BaseListData):void {
    updateLabel(data, value);
    if (value) {
      if (value is ListData) {
        super.listData = value;
      } else {
        super.listData = new ListData(value.label, null, null, value.uid, value.owner, value.rowIndex, value.columnIndex);
      }
    } else {
      super.listData = value;
    }
    _listData = value;
  }

  public function set index(value:int):void {
    _index = value;
  }

  public function get index():int {
    return _index;
  }

  public function set toolTipIndex(value:int):void {
    _toolTipIndex = value;
  }

  public function get toolTipIndex():int {
    return _toolTipIndex;
  }

  public function set selectable(value:Boolean):void {
    _selectable = value;
    if (label) {
      label.selectable = value;
    }
  }

  public function get selectable():Boolean {
    return _selectable;
  }

  protected function set listDataIcon(value:Class):void {
    if (super.listData != null) {
      (super.listData as ListData).icon = value;
    }
  }

  override public function get listData():BaseListData {
    return _listData;
  }

  public function set formatter(value:Formatter):void {
    _formatter = value;
  }

  private function redraw(value:Object):void {
    invalidateDisplayList();
  }

  override public function set data(value:Object):void {
    updateLabel(value, listData);
    if (listData && super.listData) {
      super.listData.label = listData.label;
    }
    super.data = value;
    var toolTipState:RemoteValueState;
    if (index == 1 || index == -1) {
      toolTipState = data as RemoteCompositeValueState;
    } else if (toolTipIndex >= 0) {
      toolTipState = ((data as RemoteCompositeValueState).children[toolTipIndex] as RemoteValueState);
    }
    if (toolTipState) {
      if (_toolTipChangeListener != null) {
        _toolTipChangeListener.reset(toolTipState);
        refreshToolTip(toolTipState.value);
      } else {
        _toolTipChangeListener = BindingUtils.bindSetter(refreshToolTip, toolTipState, "value", true);
      }
    }
    if (data) {
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
  }

  protected function updateLabel(rendererData:Object, rendererListData:BaseListData):void {
    if (rendererData && rendererListData) {
      var cellValueState:RemoteValueState;
      if (rendererListData.owner is DataGrid) {
        cellValueState = ((rendererData as RemoteCompositeValueState).children[index] as RemoteValueState);
        _forceSelectable = !(rendererListData.owner as DataGrid).editable;
      } else {
        cellValueState = ((rendererData as RemoteCompositeValueState).children[1] as RemoteValueState);
      }
      if (_valueChangeListener != null) {
        _valueChangeListener.reset(cellValueState);
        refresh(cellValueState.value);
      } else {
        _valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value", true);
      }
      if (_writabilityChangeListener != null) {
        _writabilityChangeListener.reset(cellValueState);
        refreshSelectability(cellValueState.writable);
      } else {
        _writabilityChangeListener = BindingUtils.bindSetter(refreshSelectability, cellValueState, "writable", true);
      }
      rendererListData.label = computeLabel(cellValueState.value);
    }
  }

  protected function computeLabel(cellValue:Object):String {
    var cellLabel:String;
    if (cellValue != null) {
      if (_formatter != null) {
        cellLabel = _formatter.format(cellValue);
      } else {
        cellLabel = cellValue.toString();
      }
    } else {
      cellLabel = null;
    }
    return cellLabel;
  }

  protected function refresh(cellValue:Object):void {
    listData.label = computeLabel(cellValue);
    if (listData && super.listData) {
      super.listData.label = listData.label;
    }
    invalidateProperties();
  }

  protected function refreshToolTip(toolTipValue:Object):void {
    if (toolTipValue != null) {
      toolTip = toolTipValue.toString();
    } else {
      toolTip = null;
    }
  }

  protected function refreshSelectability(writable:Boolean):void {
    selectable = _forceSelectable || !writable;
  }

  protected override function commitProperties():void {
    super.commitProperties();
    var cellText:String = label.text;
    if (_action != null) {
      if (HtmlUtil.isHtml(cellText)) {
        cellText = HtmlUtil.sanitizeHtml(cellText);
      }
      label.htmlText = "<u><a href='event:action'>" + cellText + "</a></u>";
    } else {
      if (HtmlUtil.isHtml(cellText)) {
        label.htmlText = HtmlUtil.sanitizeHtml(cellText);
      }
    }
    if (index != -1 && index != 1 && toolTipIndex < 0) {
      //in that case tool tip is based on the cell text.
      if (HtmlUtil.isHtml(cellText)) {
        // unconditional
        toolTip = cellText;
      } else {
        if (toolTip) {
          toolTip = cellText;
        } else {
          toolTip = null;
        }
      }
    } else if (_toolTipChangeListener != null) {
      refreshToolTip(_toolTipChangeListener.getValue());
    }
    invalidateSize();
  }

  public function set action(value:RAction):void {
    _action = value;
    if (_action != null) {
      addEventListener(FlexEvent.CREATION_COMPLETE, function (event:FlexEvent):void {
        //label.selectable = true;
        label.addEventListener(TextEvent.LINK, function (evt:TextEvent):void {
          // To ensure that the row is selected before the action gets executed.
          callLater(_actionHandler.execute, [_action]);
        });
      });
    }
  }

  public function set actionHandler(value:IActionHandler):void {
    _actionHandler = value;
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

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    if (data && listData && listData.owner is DataGrid) {
      if (backgroundIndex >= 0) {
        var backgroundValue:Object = ((data as RemoteCompositeValueState).children[backgroundIndex]
        as RemoteValueState).value;
        var g:Graphics = graphics;
        g.clear();
        if (backgroundValue != null) {
          g.beginFill(new uint(backgroundValue), DefaultFlexViewFactory.getAlphaFromArgb(backgroundValue as String));
          g.drawRect(0, 0, unscaledWidth, unscaledHeight);
          g.endFill();
        }
      }
      if (foregroundIndex >= 0) {
        var foregroundValue:Object = ((data as RemoteCompositeValueState).children[foregroundIndex]
        as RemoteValueState).value;
        if (foregroundValue) {
          setStyle("color", foregroundValue);
          alpha = DefaultFlexViewFactory.getAlphaFromArgb(foregroundValue as String);
        } else {
          setStyle("color", null);
          alpha = 1.0;
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
}
}
