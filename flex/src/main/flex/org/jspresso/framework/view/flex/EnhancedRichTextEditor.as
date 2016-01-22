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

import mx.controls.RichTextEditor;
import mx.events.FlexEvent;

import org.jspresso.framework.util.html.HtmlUtil;

public class EnhancedRichTextEditor extends RichTextEditor {

  private var _editable:Boolean = true;

  public function EnhancedRichTextEditor() {
    addEventListener(FlexEvent.CREATION_COMPLETE, function (e:FlexEvent):void {
      synchEditability();
    });
  }

  public function get xhtmlText():String {
    return HtmlUtil.convertToXHtml(this.htmlText);
  }

  public function set xhtmlText(val:String):void {
    this.htmlText = HtmlUtil.convertFromXHtml(val);
  }

  public function get editable():Boolean {
    return _editable;
  }

  public function set editable(value:Boolean):void {
    _editable = value;
    synchEditability();
  }

  protected function synchEditability():void {
    if (textArea) {
      textArea.editable = editable;
    }
    if (toolbar) {
      toolbar.visible = editable;
    }
    if (toolBar2) {
      toolBar2.visible = editable;
    }
  }
}
}
