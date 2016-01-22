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

import flash.display.Sprite;
import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.events.TimerEvent;
import flash.ui.Keyboard;
import flash.utils.Timer;

import mx.controls.DataGrid;
import mx.controls.listClasses.IDropInListItemRenderer;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.mx_internal;
import mx.events.DataGridEvent;

use namespace mx_internal;

/**
 *  DataGrid that only allows editing if you double click
 */
public class EnhancedDataGrid extends DataGrid {

  private var preventEditing:Boolean;

  private var lastClickedRow:int;
  private var lastClickedColumn:int;
  private var _savedSortIndex:int;
  private var _savedSortDirection:String;
  private var _customSort:Boolean;
  private var _cbMultiSelection:Boolean;

  private static var H_SCROLL_DELAY_MAX_VISIBLE_CELLS:int = 500;
  private var _targetHScrollPosition:Number;
  private var _hScrollTimer:Timer;

  public function EnhancedDataGrid() {
    super();
    doubleClickEnabled = true;
    preventEditing = false;
    lastClickedRow = -1;
    lastClickedColumn = -1;
    _savedSortIndex = -1;
    _customSort = false;
    _cbMultiSelection = false;
    addEventListener(DataGridEvent.ITEM_EDIT_BEGINNING, itemEditBeginning);
    _hScrollTimer = new Timer(200, 1);
    _hScrollTimer.addEventListener(TimerEvent.TIMER_COMPLETE, applyTargetHScroll);
  }

  override protected function mouseDoubleClickHandler(event:MouseEvent):void {
    preventEditing = false;
    super.mouseDoubleClickHandler(event);
  }

  override protected function mouseUpHandler(event:MouseEvent):void {
    var r:IListItemRenderer;

    r = mouseEventToItemRenderer(event);

    if (r && r is IDropInListItemRenderer) {
      var dilr:IDropInListItemRenderer = r as IDropInListItemRenderer;
      preventEditing = dilr.listData.rowIndex != lastClickedRow || dilr.listData.columnIndex != lastClickedColumn;
      lastClickedRow = dilr.listData.rowIndex;
      lastClickedColumn = dilr.listData.columnIndex;
    } else {
      preventEditing = false;
    }
    super.mouseUpHandler(event);
  }

  public function itemEditBeginning(event:DataGridEvent):void {
    // Fixes bug where datagrid does not get refreshed after an edition end due to a click in anther column.
    // see bug #1198
    collection.enableAutoUpdate();
    if (preventEditing) {
      event.preventDefault();
    }
  }

  public function set customSort(value:Boolean):void {
    _customSort = value;
  }

  public function get customSort():Boolean {
    return _customSort;
  }

  public function displaySort(sortInd:int, descending:Boolean):void {
    sortDirection = descending ? "DESC" : "ASC";

    // set the grid's sortIndex
    lastSortIndex = sortIndex;
    sortIndex = sortInd;

    // save sort information to be applied when dataProvider changes
    _savedSortIndex = sortIndex;
    _savedSortDirection = sortDirection;

    invalidateDisplayList();
  }

  override protected function collectionChangeHandler(event:Event):void {
    super.collectionChangeHandler(event);
    if (_customSort) {
      sortIndex = _savedSortIndex;
      sortDirection = _savedSortDirection;
    }
  }

  public function set cbMultiSelection(value:Boolean):void {
    _cbMultiSelection = value;
  }

  public function get cbMultiSelection():Boolean {
    return _cbMultiSelection;
  }

  override protected function selectItem(item:IListItemRenderer, shiftKey:Boolean, ctrlKey:Boolean,
                                         transition:Boolean = true):Boolean {
    // only run selection code if a checkbox was hit and always
    // pretend we're using ctrl selection
    if (_cbMultiSelection && item is SelectionCheckBoxRenderer) {
      return super.selectItem(item, false, true, transition);
    }
    return super.selectItem(item, shiftKey, ctrlKey, transition);
  }

  override protected function moveSelectionVertically(code:uint, shiftKey:Boolean, ctrlKey:Boolean):void {
    var cbms:Boolean = _cbMultiSelection;
    try {
      // disable Checkbox multi selection until the end of the vertical selection move
      _cbMultiSelection = false;
      super.moveSelectionVertically(code, shiftKey, ctrlKey);
    } finally {
      _cbMultiSelection = cbms;
    }
  }

  // whenever we draw the renderer, make sure we re-evaluate the checked state
  override protected function drawItem(item:IListItemRenderer, selected:Boolean = false, highlighted:Boolean = false,
                                       caret:Boolean = false, transition:Boolean = false):void {
    if (item is SelectionCheckBoxRenderer) {
      (item as SelectionCheckBoxRenderer).invalidateProperties();
    }
    super.drawItem(item, selected, highlighted, caret, transition);
  }

  override public function destroyItemEditor():void {
    if (itemEditorInstance is RemoteValueDgItemEditor) {
      (itemEditorInstance as RemoteValueDgItemEditor).cleanup();
    }
    super.destroyItemEditor();
  }

  /**
   *  Workarounds a NPE that occurs when a selection is made before the table is drawn.
   */
  override protected function UIDToItemRenderer(uid:String):IListItemRenderer {
    if (!(listContent && visibleData)) {
      return null;
    }
    return super.UIDToItemRenderer(uid);
  }

  override protected function keyDownHandler(event:KeyboardEvent):void {
    if (editedItemPosition == null) {
      switch (event.keyCode) {
        case Keyboard.LEFT:
          if (horizontalScrollPosition > 0) {
            horizontalScrollPosition -= 1;
          }
          break;
        case Keyboard.RIGHT:
          if (horizontalScrollPosition < columnCount - 1) {
            horizontalScrollPosition += 1;
          }
          break;
      }
    }
    super.keyDownHandler(event);
  }


  override protected function get unscaledHeight():Number {
    var uh:Number = super.unscaledHeight;
    if (isNaN(uh)) {
      return 0;
    }
    return uh;
  }

  override protected function get unscaledWidth():Number {
    var uw:Number = super.unscaledWidth;
    if (isNaN(uw)) {
      return 0;
    }
    return uw;
  }

  override public function set horizontalScrollPosition(value:Number):void {
    if ((rowCount * columnCount) > H_SCROLL_DELAY_MAX_VISIBLE_CELLS) {
      _targetHScrollPosition = value;
      _hScrollTimer.reset();
      _hScrollTimer.start();
    } else {
      super.horizontalScrollPosition = value;
    }
  }

  private function applyTargetHScroll(event:TimerEvent):void {
    super.horizontalScrollPosition = NaN;
    super.horizontalScrollPosition = _targetHScrollPosition;
  }
}
}
