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

import mx.collections.ArrayCollection;
import mx.controls.Image;
import mx.controls.List;
import mx.controls.listClasses.ListData;
import mx.controls.listClasses.ListItemRenderer;

import org.jspresso.framework.gui.remote.RIcon;

public class RIconListItemRenderer extends ListItemRenderer {

  private var _image:Image;
  private var _selectedIcon:RIcon;

  private var _labels:Array;
  private var _icons:Array;
  private var _iconTemplate:Class;
  private var _showIcon:Boolean;

  public function RIconListItemRenderer() {
    _image = new CachedImage();
    addChild(_image);
  }

  public function set labels(value:Array):void {
    _labels = value;
  }

  public function set icons(value:Array):void {
    _icons = value;
  }

  public function set iconTemplate(value:Class):void {
    _iconTemplate = value;
  }

  public function set showIcon(value:Boolean):void {
    _showIcon = value;
  }

  override public function set data(value:Object):void {
    //cannot rely on listData.rowIndex.
    var _index:int = ((owner as List).dataProvider as ArrayCollection).getItemIndex(value);
    //trace(">>> List index <<< " + _index);
    listData.label = _labels[_index];
    if (_showIcon) {
      if (!(listData as ListData).icon) {
        (listData as ListData).icon = _iconTemplate;
      }
      _selectedIcon = _icons[_index] as RIcon;
    }
    super.data = value;
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);
    if (_showIcon) {
      if (_selectedIcon != null) {
        icon.width = _selectedIcon.dimension.width;
        icon.height = _selectedIcon.dimension.height;
        _image.source = _selectedIcon.imageUrlSpec;
        label.x += (_selectedIcon.dimension.width - icon.measuredHeight);
        label.y += ((_selectedIcon.dimension.height - icon.measuredWidth) / 2);
      } else {
        _image.source = null;
      }
      _image.x = icon.x;
      _image.y = icon.y;
      _image.width = icon.width;
      _image.height = icon.height;
    } else {
      _image.visible = false;
    }
    if (icon) {
      icon.visible = false;
    }
  }

  override protected function measure():void {
    super.measure();
    if (_selectedIcon && _selectedIcon.dimension) {
      if (_selectedIcon.dimension.width > measuredWidth) {
        measuredWidth += (_selectedIcon.dimension.width - icon.measuredWidth);
      }
      if (_selectedIcon.dimension.height > measuredHeight) {
        measuredHeight += (_selectedIcon.dimension.height - icon.measuredHeight);
      }
    }
  }

}
}
