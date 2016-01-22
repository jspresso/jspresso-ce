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
import mx.controls.Label;
import mx.core.ScrollPolicy;

import org.jspresso.framework.gui.remote.RIcon;

public class RIconLabel extends HBox {

  private var _label:Label;
  private var _iconImage:Image;
  private var _labels:Object;
  private var _icons:Object;
  private var _showIcon:Boolean;
  private var _value:String;

  public function RIconLabel() {
    horizontalScrollPolicy = ScrollPolicy.OFF;
    verticalScrollPolicy = ScrollPolicy.OFF;
    _label = new Label();
    _iconImage = new CachedImage();
    addChild(_iconImage);
    addChild(_label);
  }

  public function set labels(value:Object):void {
    _labels = value;
  }

  public function set showIcon(value:Boolean):void {
    _showIcon = value;
    if (_showIcon) {
      setStyle("horizontalGap", 2);
    } else {
      setStyle("horizontalGap", 0);
    }
  }

  public function set icons(value:Object):void {
    _icons = value;
  }

  public function get value():String {
    return _value;
  }

  public function set value(value:String):void {
    _value = value;
    if (value) {
      if (_showIcon && _icons) {
        var icon:RIcon = _icons[value] as RIcon;
        if (icon) {
          if (icon.dimension) {
            _iconImage.width = icon.dimension.width;
            _iconImage.height = icon.dimension.height;
          }
          _iconImage.source = icon.imageUrlSpec;
        } else {
          _iconImage.source = null;
        }
      } else {
        _iconImage.visible = false;
      }
      if (_labels) {
        var text:String = _labels[value] as String;
        if (text) {
          _label.text = text;
        } else {
          _label.text = "";
        }
      }
    } else {
      _iconImage.source = null;
      _label.text = "";
    }
  }
}
}
