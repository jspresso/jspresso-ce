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

import mx.controls.Image;
import mx.controls.listClasses.BaseListData;

import org.jspresso.framework.gui.remote.RIcon;

public class EnumerationDgItemRenderer extends RemoteValueDgItemRenderer {

  private var _image:Image;
  private var _selectedIcon:RIcon;

  private var _values:Array;
  private var _labels:Array;
  private var _icons:Array;
  private var _iconTemplate:Class;
  private var _showIcon:Boolean;

  public function EnumerationDgItemRenderer() {
    _image = new CachedImage();
    addChild(_image);
  }

  public function set values(value:Array):void {
    _values = value;
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

  override public function set listData(value:BaseListData):void {
    value.label = _labels[value.rowIndex];
    super.listData = value;
    if (_showIcon) {
      listDataIcon = _iconTemplate;
      computeIcon(value.rowIndex);
    }
  }

  override protected function computeLabel(cellValue:Object):String {
    var valIndex:int = _values.indexOf(cellValue);
    if (_showIcon) {
      computeIcon(valIndex);
    }
    if (cellValue != null) {
      return _labels[valIndex];
    } else {
      return super.computeLabel(cellValue);
    }
  }

  private function computeIcon(iconIndex:int):void {
    if (_showIcon) {
      _selectedIcon = _icons[iconIndex] as RIcon;
    }
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);
    if (_showIcon) {
      if (_selectedIcon != null) {
        icon.width = _selectedIcon.dimension.width;
        icon.height = _selectedIcon.dimension.height;
        _image.source = _selectedIcon.imageUrlSpec;
        label.x += (_selectedIcon.dimension.width - icon.measuredHeight);
        label.y += ((_selectedIcon.dimension.height - icon.measuredWidth) /2);
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

  override protected function refresh(cellLabel:Object):void {
    super.refresh(cellLabel);
    invalidateDisplayList();
  }
}
}
