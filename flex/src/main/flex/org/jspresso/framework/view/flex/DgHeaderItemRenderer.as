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

import mx.containers.HBox;
import mx.controls.Image;
import mx.controls.listClasses.BaseListData;
import mx.controls.listClasses.IDropInListItemRenderer;
import mx.core.ScrollPolicy;

import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.util.html.HtmlUtil;

public class DgHeaderItemRenderer extends HBox implements IColumnIndexProvider, IDropInListItemRenderer {

  private var _index:int;
  private var _viewFactory:DefaultFlexViewFactory;
  private var _rTemplate:RComponent;
  private var _text:HeaderText;

  public function DgHeaderItemRenderer() {
    _index = -1;
    _text = new HeaderText();
    _text.selectable = false;
    horizontalScrollPolicy = ScrollPolicy.OFF;
    verticalScrollPolicy = ScrollPolicy.OFF;
    setStyle("paddingLeft", 5);
  }

  public function set index(value:int):void {
    _index = value;
  }

  public function get index():int {
    return _index;
  }

  public function set viewFactory(value:DefaultFlexViewFactory):void {
    _viewFactory = value;
    configure();
  }

  public function set rTemplate(value:RComponent):void {
    _rTemplate = value;
    configure();
  }

  private function configure():void {
    if (_viewFactory != null && _rTemplate != null) {
      _viewFactory.applyComponentStyle(_text, _rTemplate);
      if (_rTemplate.icon) {
        var labelIcon:Image = new CachedImage();
        labelIcon.source = _rTemplate.icon.imageUrlSpec;
        addChild(labelIcon);
      }
      addChild(_text);
    }
  }

  protected override function commitProperties():void {
    super.commitProperties();
    var cellText:String = _text.text;
    if (HtmlUtil.isHtml(cellText)) {
      _text.htmlText = HtmlUtil.sanitizeHtml(cellText);
    }
    _text.setTextFieldWordWrap(false);
  }

  protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);
    _text.setTextFieldWordWrap(false);
  }

  public override function set data(value:Object):void {
    _text.data = value;
    super.data = value;
  }

  public function get listData():BaseListData {
    return _text.listData;
  }

  public function set listData(value:BaseListData):void {
    _text.listData = value;
  }
}
}

import mx.controls.Text;

internal class HeaderText extends Text {

  public function setTextFieldWordWrap(value:Boolean):void {
    if (textField) {
      textField.wordWrap = value;
    }
  }
}
