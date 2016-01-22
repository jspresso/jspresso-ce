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
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.text.TextField;

import mx.controls.CheckBox;
import mx.controls.dataGridClasses.DataGridListData;
import mx.controls.listClasses.ListBase;

/**
 *  The Renderer.
 */
public class SelectionCheckBoxRenderer extends CheckBox {

  public function SelectionCheckBoxRenderer() {
    focusEnabled = false;
  }

  override public function set data(value:Object):void {
    super.data = value;
    invalidateProperties();
  }

  override protected function commitProperties():void {
    super.commitProperties();
    if (owner is ListBase) {
      selected = ListBase(owner).isItemSelected(data);
    }
  }

  /* eat keyboard events, the underlying list will handle them */
  override protected function keyDownHandler(event:KeyboardEvent):void {
  }

  /* eat keyboard events, the underlying list will handle them */
  override protected function keyUpHandler(event:KeyboardEvent):void {
  }

  /* eat mouse events, the underlying list will handle them */
  override protected function clickHandler(event:MouseEvent):void {
  }

  /* center the checkbox if we're in a datagrid */
  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    if (listData is DataGridListData) {
      var n:int = numChildren;
      for (var i:int = 0; i < n; i++) {
        var c:DisplayObject = getChildAt(i);
        if (!(c is TextField)) {
          c.x = (w - c.width) / 2;
          c.y = 0;
        }
      }
    }
  }
}
}
