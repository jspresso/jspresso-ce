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

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ListCollectionView;
import mx.containers.Canvas;
import mx.controls.CheckBox;
import mx.controls.DataGrid;
import mx.controls.listClasses.BaseListData;
import mx.controls.listClasses.IDropInListItemRenderer;
import mx.core.ScrollPolicy;

public class SelectionHeaderRenderer extends Canvas implements IDropInListItemRenderer {

  private var _listData:BaseListData;
  private var _dataGrid:DataGrid;
  private var _cb:CheckBox;

  public function SelectionHeaderRenderer() {
    horizontalScrollPolicy = ScrollPolicy.OFF;
    verticalScrollPolicy = ScrollPolicy.OFF;
    setStyle("paddingLeft", 5);
    _cb = new CheckBox();
    _cb.setStyle("horizontalCenter", 0);
    _cb.setStyle("verticalCenter", 0);
    _cb.addEventListener(MouseEvent.CLICK, onCBchange);
    addChild(_cb);
  }

  [Bindable("dataChange")]
  public function get listData():BaseListData {
    return _listData;
  }

  public function set listData(value:BaseListData):void {
    _listData = value;
    _dataGrid = value.owner as DataGrid;
    BindingUtils.bindSetter(function (selectedItems:Array):void {
      var l:Number = 0;
      if (_dataGrid.dataProvider) {
        l = (_dataGrid.dataProvider as ListCollectionView).length;
      }
      _cb.selected = (selectedItems.length == l);
    }, _dataGrid, "selectedItems", true);
  }


  private function onCBchange(event:Event):void {
    if (_cb.selected && _dataGrid.dataProvider) {
      _dataGrid.selectedItems = (_dataGrid.dataProvider as ListCollectionView).toArray();
    } else {
      _dataGrid.selectedItems = [];
    }
  }
}
}
